package sketchwars.map;

import java.awt.Color;
import java.awt.image.BufferedImage;
import sketchwars.graphics.GraphicsObject;
import sketchwars.game.GameObject;
import sketchwars.graphics.Texture;
import sketchwars.physics.*;
import sketchwars.scenes.Camera;
import sketchwars.util.Converter;
import sketchwars.util.GraphicsToImage;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public abstract class AbstractMap implements GraphicsObject, GameObject {
    private final MapCollider mapCollider;
    private Texture background;
    private Texture foreground;
    private final BufferedImage foregroundImage;

    private final Camera camera;
    
    public AbstractMap(Camera camera, MapCollider mapCollider, Texture background, 
                       Texture foreground, BufferedImage foregroundImage) {
        this.camera = camera;
        this.mapCollider = mapCollider;
        this.background = background;
        this.foreground = foreground;
        this.foregroundImage = foregroundImage;
    }

    @Override
    public void update(double elapsedMillis) 
    {}

    @Override
    public boolean hasExpired()
    {
        return false;
    }

    public PixelCollider getMapCollider() {
        return mapCollider;
    }

    public Texture getForeground() {
        return foreground;
    }

    public BufferedImage getForegroundImage() {
        return foregroundImage;
    }
    
    public void setBackground(Texture background) {
        this.background = background;
    }
    
    @Override
    public void render() {
        float width = camera.getWorldWidth();
        float height = camera.getWorldHeight();
        float x = camera.getWorldLeft() + width/2.0f;
        float y = camera.getWorldTop() - height/2.0f;
        
        background.draw(null, x, y, width, height);
        foreground.draw(null, x, y, width, height);
    }

    public void dispose() {
        background.dispose();
        foreground.dispose();
    }

    public boolean updateTexture(BufferedImage subImage, boolean erase, float posX, float posY, float width, float height) {  
        //opengl coordinates to image coordinates
        GraphicsToImage glToImg = new GraphicsToImage(foregroundImage);
        int subNewWidth = (int)glToImg.transformWidth(width);
        int subNewHeight = (int)glToImg.transformHeight(height);
        int xImage = (int)(glToImg.transformX(posX) - subNewWidth/2);
        int yImage = (int)(glToImg.transformY(posY) - subNewHeight/2);
        
        //get ratios to resize sub image
        int subWidth = subImage.getWidth();
        int subHeight = subImage.getHeight();
        float widthRatio = (float) subWidth / subNewWidth;
        float heightRatio = (float) subHeight / subNewHeight;
        
        BoundingBox subImageBounds = new BoundingBox(yImage, xImage,
                yImage + subNewHeight - 1, xImage + subNewWidth - 1);
        
        BoundingBox fgBounds = new BoundingBox(0, 0, foregroundImage.getHeight() - 1, foregroundImage.getWidth() - 1);
        BoundingBox intersection = fgBounds.intersection(subImageBounds);
        //no part of the foreground image will be affected.
        if(intersection == BoundingBox.EMPTY) {return false;}
        
        //get new image coordinates
        int clippedX = intersection.getLeft();
        int clippedY = intersection.getTop();
        int clippedWidth = intersection.getWidth();
        int clippedHeight = intersection.getHeight();
        
        int subImageOffsetX = (xImage < 0) ? -xImage : 0;
        int subImageOffsetY = (yImage < 0) ? -yImage : 0;
                
        for (int i = 0; i < clippedWidth; i++) {
            for (int j = 0; j < clippedHeight; j++) {
                int imageI = (int) Math.round((i + subImageOffsetX) * widthRatio);
                int imageJ = (int) Math.round((j + subImageOffsetY) * heightRatio);

                int xSet = clippedX + i;
                int ySet = clippedY + j;

                int color = subImage.getRGB(imageI, imageJ);
                int alpha = color >> 24;

                if (alpha != 0) {  
                    
                    if (erase) {
                        foregroundImage.setRGB(xSet, ySet, Color.TRANSLUCENT);
                    } else {
                        foregroundImage.setRGB(xSet, ySet, color);
                    }
                }
            }
        }
        
        BufferedImage replacedRegion = foregroundImage.getSubimage(clippedX, clippedY, clippedWidth, clippedHeight);
        return foreground.setSubTexture(replacedRegion, clippedX, clippedY, clippedWidth, clippedHeight);
    }

    public void updateInPhysics(BufferedImage subImage, boolean erase, float xStart, float yStart, float width, float height) {
        BitMask mapBitmask = mapCollider.getPixels();
        
        int widthPhysics = Converter.GraphicsToPhysicsX(width);
        int heightPhysics = Converter.GraphicsToPhysicsY(height);
        
        int xPhysics = Converter.GraphicsToPhysicsX(xStart) - widthPhysics/2;
        int yPhysics = Converter.GraphicsToPhysicsY(yStart) - heightPhysics/2;
        
        BoundingBox bb = new BoundingBox(yPhysics, xPhysics, yPhysics + heightPhysics, xPhysics + widthPhysics);
        
        if (erase) {
            BitMaskFactory.updateFromImageAlpha(subImage, mapBitmask, bb, false);
        } else {
            BitMaskFactory.updateFromImageAlpha(subImage, mapBitmask, bb, true);
        }
    }
}

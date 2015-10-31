package sketchwars.map;

import java.awt.Color;
import java.awt.image.BufferedImage;
import sketchwars.graphics.GraphicsObject;
import sketchwars.game.GameObject;
import sketchwars.graphics.Texture;
import sketchwars.physics.*;
import sketchwars.util.OpenGLToImage;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public abstract class AbstractMap implements GraphicsObject, GameObject {
    private final MapCollider mapCollider;
    private Texture background;
    private final Texture foreground;
    private final BufferedImage foregroundImage;

    public AbstractMap(MapCollider mapCollider, Texture background, Texture foreground, BufferedImage foregroundImage) {
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
        background.draw(null, 0, 0, 2, 2);
        foreground.draw(null, 0, 0, 2, 2);
    }

    public void dispose() {
        background.dispose();
        foreground.dispose();
    }

    public boolean updateTexture(BufferedImage subImage, boolean erase, float posX, float posY, float width, float height) {  
        //opengl coordinates to image coordinates
        OpenGLToImage glToImg = new OpenGLToImage(foregroundImage);
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
        if(intersection == BoundingBox.EMPTY) { return false;}
        
        //get new image coordinates
        xImage = intersection.getLeft();
        yImage = intersection.getTop();
        subNewWidth = intersection.getRight() - intersection.getLeft();
        subNewHeight = intersection.getBottom() - intersection.getTop();
        
        for (int i = 0; i < subNewWidth; i++) {
            for (int j = 0; j < subNewHeight; j++) {
                int imageI = (int) ((float)i * widthRatio);
                int imageJ = (int) ((float)j * heightRatio);

                int xSet = xImage + i;
                int ySet = yImage + j;

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
        BufferedImage replacedRegion = foregroundImage.getSubimage(xImage, yImage, subNewWidth, subNewHeight);
        return foreground.setSubTexture(replacedRegion, xImage, yImage, replacedRegion.getWidth(), replacedRegion.getHeight());
    }

    public void updateInPhysics(BufferedImage subImage, boolean erase, float xStart, float yStart, float width, float height) {
        BitMask mapBitmask = mapCollider.getPixels();
        
        int widthPhysics = (int)(width * 1024.0);
        int heightPhysics = (int)(height * 1024.0);
        
        int xPhysics = (int) (xStart * 1024.0) - widthPhysics/2;
        int yPhysics = (int) (yStart * 1024.0) - heightPhysics/2;
        
        BoundingBox bb = new BoundingBox(yPhysics, xPhysics, yPhysics + heightPhysics, xPhysics + widthPhysics);
        
        if (erase) {
            BitMaskFactory.updateFromImageAlpha(subImage, mapBitmask, bb, false);
        } else {
            BitMaskFactory.updateFromImageAlpha(subImage, mapBitmask, bb, true);
        }
    }
}

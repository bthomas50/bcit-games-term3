package sketchwars.map;

import java.awt.Color;
import java.awt.image.BufferedImage;
import org.joml.Vector2d;
import sketchwars.OpenGL;
import sketchwars.graphics.GraphicsObject;
import sketchwars.game.GameObject;
import sketchwars.graphics.Texture;
import sketchwars.physics.*;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public abstract class AbstractMap implements GraphicsObject, GameObject {
    private final PixelCollider mapCollider;
    private Texture background;
    private final Texture foreground;
    private final BufferedImage foregroundImage;

    public AbstractMap(PixelCollider mapCollider, Texture background, Texture foreground, BufferedImage foregroundImage) {
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

    public boolean updateTexture(BufferedImage subImage, boolean erase, float xStart, float yStart, float width, float height) {
        int widthFG = foregroundImage.getWidth();
        int heightFG = foregroundImage.getHeight();
                
        int subWidth = subImage.getWidth();
        int subHeight = subImage.getHeight();

        Vector2d screen = OpenGL.getDisplaySize();
        float widthRatioFG = (float)(widthFG/screen.x);
        float heightRatioFG = (float)(heightFG/screen.y);
        
        int subNewWidth = (int)(widthRatioFG * screen.x * width/2.0);
        int subNewHeight = (int)(heightRatioFG * screen.y * height/2.0);
        int xImage = (int)(widthRatioFG * (screen.x/2.0)*(xStart + 1.0)) - subNewWidth/2;
        int yImage = (int)(heightRatioFG * (screen.y/2.0)*(2.0 - (yStart + 1.0))) - subNewHeight/2;
        
        float widthRatio = (float) subWidth / subNewWidth;
        float heightRatio = (float) subHeight / subNewHeight;
        
        if (xImage >= 0 && yImage >= 0 && (xImage + subNewWidth) < widthFG && (yImage + subNewHeight) < heightFG) {
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
            // Causes crash due to my computer not having opengl 4.5 compatible drivers
            // BufferedImage replacedRegion = foregroundImage.getSubimage(xImage, yImage, subNewWidth, subNewHeight);
            // return foreground.setSubTexture(replacedRegion, xImage, yImage, replacedRegion.getWidth(), replacedRegion.getHeight());
            return true;
        }
        return false;
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

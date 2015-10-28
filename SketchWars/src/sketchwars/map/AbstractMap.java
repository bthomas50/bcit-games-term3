package sketchwars.map;

import java.awt.Color;
import java.awt.image.BufferedImage;
import org.joml.Vector2d;
import sketchwars.OpenGL;
import sketchwars.graphics.GraphicsObject;
import sketchwars.game.GameObject;
import sketchwars.graphics.Texture;
import sketchwars.physics.*;
import sketchwars.util.PhysicsToImage;
import static java.lang.Math.min;
import static java.lang.Math.max;

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

    public boolean updateTexture(BufferedImage subImage, boolean erase, long vCenter) {

        int subImageWidth = subImage.getWidth();
        int subImageHeight = subImage.getHeight();
        
        PhysicsToImage ph2im = new PhysicsToImage(foregroundImage);

        int top = Vectors.iyComp(vCenter) - (subImageHeight / 2);
        int left = Vectors.ixComp(vCenter) - (subImageWidth / 2);

        BoundingBox bounds = new BoundingBox(
            (int)ph2im.transformY(top),
            (int)ph2im.transformX(left),
            (int)ph2im.transformY(top + subImageHeight - 1),
            (int)ph2im.transformX(left + subImageWidth - 1));
        BoundingBox fgBounds = new BoundingBox(0, 0, foregroundImage.getHeight() - 1, foregroundImage.getWidth() - 1);
        BoundingBox intersection = fgBounds.intersection(bounds);
        //no part of the foreground image will be affected.
        if(intersection == BoundingBox.EMPTY) {
            return false;
        }

        for(int x = intersection.getLeft(); x <= intersection.getRight(); x++) {
            int subImageX = (int) ph2im.invTransformX(x) - left;
            if(!isXInBounds(subImage, subImageX) || !isXInBounds(foregroundImage, x)) {
                continue;
            }
            for(int y = intersection.getTop(); y <= intersection.getBottom(); y++) {
                int subImageY = (int) ph2im.invTransformY(y) - top;
                if(!isYInBounds(subImage, subImageY) || !isYInBounds(foregroundImage, y)) {
                    continue;
                }
                int color = subImage.getRGB(subImageX, subImageY);
                int alpha = color >> 24;
                if(alpha != 0) {
                    if (erase) {
                        foregroundImage.setRGB(x, y, Color.TRANSLUCENT);
                    } else {
                        foregroundImage.setRGB(x, y, color);
                    }
                }
            }
        }
        BufferedImage replacedRegion = foregroundImage.getSubimage(intersection.getLeft(), intersection.getTop(), intersection.getWidth(), intersection.getHeight());
        return foreground.setSubTexture(replacedRegion, intersection.getLeft(), intersection.getTop(), intersection.getWidth(), intersection.getHeight());
    }

    private static boolean isXInBounds(BufferedImage image, int x) {
        return x >= 0 && x < image.getWidth();
    }

    private static boolean isYInBounds(BufferedImage image, int y) {
        return y >= 0 && y < image.getHeight();
    }

    public void updateInPhysics(BufferedImage subImage, boolean erase, long vCenter) {
        BitMask mapBitmask = mapCollider.getPixels();
        
        int top = Vectors.iyComp(vCenter) - (subImage.getHeight() / 2);
        int left = Vectors.ixComp(vCenter) - (subImage.getWidth() / 2);
        
        BitMaskFactory.updateFromImageAlpha(subImage, mapBitmask, Vectors.create(left, top), !erase);
        //mapCollider.setPixels(BitMaskFactory.createFromImageAlpha(foregroundImage, mapCollider.getBounds()));
    }
}

package sketchwars.map;

import java.awt.image.BufferedImage;
import sketchwars.graphics.GraphicsObject;
import sketchwars.game.GameObject;
import sketchwars.graphics.Texture;
import sketchwars.physics.PixelCollider;

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
}

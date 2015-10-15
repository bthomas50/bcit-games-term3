package sketchwars.animation;

import sketchwars.game.GameObject;
import sketchwars.graphics.GraphicsObject;
import sketchwars.graphics.Texture;
import sketchwars.physics.Vectors;

/**
 * This for the alpha grenade explosion (will re-factor and improve after alpha)
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class Animation implements GraphicsObject, GameObject {
    private long Position;
    private long dimension;
    protected Texture texture;
    
    protected boolean startAnimation;
    /**
     * In milliseconds
     */
    protected double duration;
    protected double elapsed;
    
    @Override
    public void render() {
        if (!hasExpired()) {
            double posX = Vectors.xComp(Position) / 1024.0;
            double posY = Vectors.yComp(Position) / 1024.0;
        
            double width = (double) Vectors.xComp(dimension) / 2048.0;
            double height = (double) Vectors.yComp(dimension) / 2048.0;
        
            texture.drawNormalized(posX, posY, width, height);
        }
    }

    public void setPosition(long Position) {
        this.Position = Position;
    }

    public void setDimension(long dimension) {
        this.dimension = dimension;
    }
    
    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void update(double delta) {
        if (startAnimation) {
            elapsed += delta;
        }
    }
    
    public boolean hasExpired() {
        return elapsed > duration;
    }
    
    public void start() {
        startAnimation = true;
    }
    
    public void pause() {
        startAnimation = false;
    }
    
    public void stop() {
        startAnimation = false;
        elapsed = 0;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
    
    public void setExpired(boolean expired) {
        if (expired) {
            elapsed = duration + 1;
        } else {
            elapsed = 0;
        }
    }
}

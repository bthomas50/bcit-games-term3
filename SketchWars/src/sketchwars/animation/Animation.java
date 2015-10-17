package sketchwars.animation;

import java.awt.image.BufferedImage;
import org.joml.Vector2d;
import sketchwars.exceptions.AnimationException;
import sketchwars.game.GameObject;
import sketchwars.graphics.GraphicsObject;
import sketchwars.graphics.Texture;

/**
 * This for the alpha grenade explosion (will re-factor and improve after alpha)
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class Animation implements GraphicsObject, GameObject {
    private Vector2d position;
    private Vector2d dimension;
    
    protected int totalSprites;
    private final double frameLength;       
    protected float spriteWidth;
    protected float spriteHeight;
    
    protected boolean startAnimation;
    
    /**
     * In milliseconds
     */
    protected double duration;
    protected double elapsed;
    protected boolean loop;
    
    private final Texture[] frams;
    
    /**
     * Load an animation 
     * @param spriteSheet Animation sprite sheet
     * @param totalSprites sprite count
     * @param duration total animation play time in milliseconds
     * @param loop loop the animation
     * @throws sketchwars.exceptions.AnimationException any load errors
     */
    public Animation(BufferedImage spriteSheet, int totalSprites, double duration, boolean loop) throws AnimationException {
        if (duration == 0) {
            throw new AnimationException("Duration cannot be 0.");
        } else if (spriteSheet == null) {
            throw new AnimationException("SpriteSheet cannot be a null pointer.");
        } else if (totalSprites == 0) {
            throw new AnimationException("Sprite count cannot be 0.");
        } else if (spriteSheet.getWidth() < totalSprites || spriteSheet.getHeight() < 1) {
            throw new AnimationException("Given sprite sheet is too small.");
        }
        
        this.totalSprites = totalSprites;
        this.duration = duration;
        
        frameLength = duration/totalSprites;
        
        frams = loadFrames(spriteSheet, totalSprites);
        
        this.loop = loop;
        
        position = new Vector2d();
        dimension = new Vector2d();
    }
    
    @Override
    public void render() {
        if (!hasExpired() && totalSprites > 0) {
            int currentFrame = (int)(elapsed/frameLength);

            if (currentFrame >= 0 && currentFrame < totalSprites) {
                Texture frame = frams[currentFrame];
                frame.drawNormalized(position.x, position.y, dimension.x, dimension.y);
            }
        }
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public void setDimension(Vector2d dimension) {
        this.dimension = dimension;
    }
    
    @Override
    public void update(double delta) {
        if (startAnimation) {
            elapsed += delta;
        }
    }
    
    public boolean hasExpired() {
        if (loop && elapsed > duration) {
            elapsed = 0;
        }
        
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

    private Texture[] loadFrames(BufferedImage spriteSheet, int totalSprites) {
        Texture frame[] = new Texture[totalSprites];
        spriteWidth = spriteSheet.getWidth()/(float)totalSprites;
        spriteHeight = spriteSheet.getHeight();
        
        for (int i = 0; i < totalSprites; i++) {
            int posX = (int)(i * spriteWidth);
            
            BufferedImage frameImg = spriteSheet.getSubimage(posX, 0,
                    (int)spriteWidth, (int)spriteHeight);
            Texture texture = new Texture(frameImg, true);
            frame[i] = texture;
        }
        
        return frame;
    }

    public Texture[] getFrames() {
        return frams;
    }

    public int getFrameCount() {
        return totalSprites;
    }

    public Vector2d getPosition() {
        return position;
    }

    public Vector2d getDimension() {
        return dimension;
    }
}

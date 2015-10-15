package sketchwars.animation;

import org.joml.Matrix3d;
import org.joml.Vector2d;
import sketchwars.OpenGL;
import sketchwars.exceptions.AnimationException;
import sketchwars.game.GameObject;
import sketchwars.graphics.GraphicsObject;
import sketchwars.graphics.Texture;
import sketchwars.physics.Vectors;
import sketchwars.util.CoordinateSystem;

/**
 * This for the alpha grenade explosion (will re-factor and improve after alpha)
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class Animation implements GraphicsObject, GameObject {
    private Vector2d position;
    private Vector2d dimension;
    
    protected Texture spriteSheet;
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
    
    /**
     * Load an animation 
     * @param spriteSheet Animation sprite sheet
     * @param totalSprites sprite count
     * @param duration total animation play time in milliseconds
     * @param loop loop the animation
     * @throws sketchwars.exceptions.AnimationException any load errors
     */
    public Animation(Texture spriteSheet, int totalSprites, double duration, boolean loop) throws AnimationException {
        if (duration == 0) {
            throw new AnimationException("Duration cannot be 0.");
        } else if (spriteSheet == null) {
            throw new AnimationException("SpriteSheet cannot be a null pointer.");
        } else if (totalSprites == 0) {
            throw new AnimationException("Sprite count cannot be 0.");
        } else if (spriteSheet.getTextureID() == -1) {
            throw new AnimationException("Given sprite sheet has nothing in it.");
        }
        
        this.spriteSheet = spriteSheet;
        this.totalSprites = totalSprites;
        this.duration = duration;
        
        frameLength = duration/totalSprites;
        spriteWidth = spriteSheet.getTextureWidth()/totalSprites;
        spriteHeight = spriteSheet.getTextureHeight();
        
        this.loop = loop;
    }
    
    @Override
    public void render() {
        if (!hasExpired() && totalSprites > 0) {
            int currentFrame = (int)(elapsed/frameLength);

            Matrix3d transformation = new Matrix3d();
            transformation.translation(position);
            transformation.scale(dimension.x, dimension.y, 1);
            
            float xTexCoordStart = (1.0f/totalSprites) * currentFrame;
            float xTexCoordEnd = (1.0f/totalSprites) * (currentFrame + 1);
            
            xTexCoordEnd = (xTexCoordEnd > 1) ? 1 : xTexCoordEnd;
            
            Vector2d textCoords[] = new Vector2d[4];
            textCoords[0] = new Vector2d(xTexCoordStart, 0);
            textCoords[1] = new Vector2d(xTexCoordStart, 1);
            textCoords[2] = new Vector2d(xTexCoordEnd, 1);
            textCoords[3] = new Vector2d(xTexCoordEnd, 0);
            
            spriteSheet.draw(textCoords, transformation);
        }
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public void setDimension(Vector2d dimension) {
        this.dimension = dimension;
    }
    
    public Texture getTexture() {
        return spriteSheet;
    }

    public void setTexture(Texture texture) {
        this.spriteSheet = texture;
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
}

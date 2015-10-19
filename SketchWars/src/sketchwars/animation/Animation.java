package sketchwars.animation;

import org.joml.Matrix3d;
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
    
    protected Texture spriteSheet;
    protected int xTotalSprites;
    protected int yTotalSprites;
    protected int totalSprites;
    private final float frameLength;       
    protected float spriteWidth;
    protected float spriteHeight;
    
    protected boolean startAnimation;
    
    /**
     * In milliseconds
     */
    protected float duration;
    protected float elapsed;
    protected float startAfter;
    protected boolean loop;
    
    /**
     * Load an animation 
     * @param spriteSheet Animation sprite sheet
     * @param totalSprites total sprites
     * @param xTotalSprites total sprites in the x axis
     * @param yTotalSprites total sprites in the y axis
     * @param duration total animation play time in milliseconds
     * @param loop loop the animation
     * @throws sketchwars.exceptions.AnimationException any load errors
     */
    public Animation(Texture spriteSheet, int totalSprites, int xTotalSprites, int yTotalSprites,  float duration, boolean loop) throws AnimationException {
        if (duration == 0) {
            throw new AnimationException("Duration cannot be 0.");
        } else if (spriteSheet == null) {
            throw new AnimationException("SpriteSheet cannot be a null pointer.");
        } else if (totalSprites == 0) {
            throw new AnimationException("Sprite count cannot be 0.");
        } else if (spriteSheet.getTextureID() == -1) {
            throw new AnimationException("Given sprite sheet has nothing in it.");
        } else if (totalSprites > (xTotalSprites * yTotalSprites)) {
            throw new AnimationException("Given total sprites is greater than available sprites.");
        }
        
        this.totalSprites = totalSprites;
        this.xTotalSprites = xTotalSprites;
        this.yTotalSprites = yTotalSprites;
        this.spriteSheet = spriteSheet;
        this.duration = duration;
        
        frameLength = duration/totalSprites;
        
        this.loop = loop;
        
        position = new Vector2d();
        dimension = new Vector2d();
        
        spriteWidth = spriteSheet.getTextureWidth()/xTotalSprites;
        spriteHeight = spriteSheet.getTextureHeight()/yTotalSprites;
    }
    
    @Override
    public void render() {
        if (!hasExpired() && totalSprites > 0) {
            int currentFrame = (int)(elapsed/frameLength);

            if (currentFrame >= 0 && currentFrame < totalSprites) {
                int yCurrentFrame = (int)Math.floor(currentFrame/xTotalSprites);
                int xCurrentFrame = currentFrame - (yCurrentFrame * xTotalSprites); 
                
                float xTexCoordStart = (1.0f/xTotalSprites) * xCurrentFrame;
                float yTexCoordStart = (1.0f/yTotalSprites) * yCurrentFrame;
                float xTexCoordEnd = (1.0f/xTotalSprites) * (xCurrentFrame + 1);
                float yTexCoordEnd = (1.0f/yTotalSprites) * (yCurrentFrame + 1);

                Vector2d textCoords[] = new Vector2d[4];
                textCoords[0] = new Vector2d(xTexCoordStart, yTexCoordStart);
                textCoords[1] = new Vector2d(xTexCoordStart, yTexCoordEnd);
                textCoords[2] = new Vector2d(xTexCoordEnd, yTexCoordEnd);
                textCoords[3] = new Vector2d(xTexCoordEnd, yTexCoordStart);
                
                
                spriteSheet.draw(textCoords, (float)position.x, (float)position.y, (float)dimension.x, (float)dimension.y);
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
        } else if (startAfter > 0) {
            elapsed += delta;
            
            if (elapsed > startAfter) {
                elapsed = 0;
                start();
            }
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

    public void setDuration(float duration) {
        this.duration = duration;
    }
    
    public void setExpired(boolean expired) {
        if (expired) {
            elapsed = duration + 1;
        } else {
            elapsed = 0;
        }
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

    /**
     * delayed start
     * @param startAfter delay in milliseconds
     */
    public void start(int startAfter) {
        this.startAfter = startAfter;
    }
    
    /**
     * get single sprite width
     * @return 
     */
    public double getSpriteWidth() {
        return spriteWidth;
    }
    
    /**
     * get single sprite height
     * @return 
     */
    public double getSpriteHeight() {
        return spriteHeight;
    }

}

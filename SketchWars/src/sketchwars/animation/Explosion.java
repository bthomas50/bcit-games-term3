package sketchwars.animation;

import sketchwars.graphics.Texture;

/**
 * This for the alpha grenade explosion (will re-factor and improve after alpha)
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class Explosion extends Animation {
    private static final int DURATION = 500;
    
    public Explosion() {
        setTexture(Texture.loadTexture("content/animation/explosion.png"));
        setDuration(DURATION);
    }  
}

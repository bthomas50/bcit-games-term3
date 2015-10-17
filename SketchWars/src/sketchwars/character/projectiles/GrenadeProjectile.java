package sketchwars.character.projectiles;

import sketchwars.graphics.Texture;
import sketchwars.character.*;
import sketchwars.util.Timer;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class GrenadeProjectile extends BasicProjectile {
    private static final int DAMAGE = 25;
    public static final double COLLIDER_RADIUS = 32.0;
    public static final double EXPLOSION_RADIUS = 128.0;
    
    private static final double LIFESPAN_MILLIS = 5000;
    private Timer timer;
    private double colliderRadius;
    private double explosionRadius;
    
    public GrenadeProjectile(Texture texture) {
        super(texture, null, DAMAGE);
        
        timer = new Timer(LIFESPAN_MILLIS);
        timer.start();
    }

    @Override
    public void update(double elapsedMillis)
    {
        timer.update(elapsedMillis);
    }

    @Override 
    public boolean hasExpired()
    {
        return timer.hasElapsed();
    }
    //Grenade doesn't do any damage!
    @Override
    protected void handleCollisionWithCharacter(SketchCharacter ch) {}
}

package sketchwars.character.projectiles;

import sketchwars.graphics.Texture;
import sketchwars.character.*;
import sketchwars.util.Timer;
import sketchwars.physics.*;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class GrenadeProjectile extends AbstractProjectile {
    private static final int EXPLOSION_DAMAGE = 25;
    public static final double COLLIDER_RADIUS = 32.0;
    public static final double EXPLOSION_RADIUS = 128.0;
    private static final float EXPLOSION_GFX_RADIUS = 0.36f;
    
    private static final double LIFESPAN_MILLIS = 5000;
    private ProjectileFactory factory;
    private Timer timer;
    
    public GrenadeProjectile(Texture texture, ProjectileFactory factory) {
        super(texture, null, 0);
        this.factory = factory;
        timer = new Timer(LIFESPAN_MILLIS);
        timer.start();
    }

    @Override
    public void update(double elapsedMillis)
    {
        timer.update(elapsedMillis);
        if(hasExpired())
        {
            factory.createExplosion(coll.getPosition(), EXPLOSION_RADIUS, EXPLOSION_DAMAGE);
        }
    }

    @Override 
    public boolean hasExpired()
    {
        return timer.hasElapsed();
    }
    //Grenade doesn't do anything special
    @Override
    protected void handleCollisionWithCharacter(SketchCharacter ch) {}
    //Grenade doesn't do anything special
    @Override
    protected void handleCollision(Collider c) {}
}

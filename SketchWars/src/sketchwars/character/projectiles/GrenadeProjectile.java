package sketchwars.character.projectiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
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
    public static final double EXPLOSION_RADIUS = 120.0;
    
    private static final double LIFESPAN_MILLIS = 5000;
    private final ProjectileFactory factory;
    private final Timer timer;
    private final BufferedImage explosionAlpha;
       
    public GrenadeProjectile(Texture texture, ProjectileFactory factory) {
        super(texture, null, 0);
        this.factory = factory;
        timer = new Timer(LIFESPAN_MILLIS);
        timer.start();
        
        int radius = (int)EXPLOSION_RADIUS;
        explosionAlpha = new BufferedImage(radius*2, radius*2, BufferedImage.TYPE_INT_ARGB);
        Graphics g = explosionAlpha.getGraphics();
        g.fillOval(0, 0, radius*2, radius*2);
    }

    @Override
    public void update(double elapsedMillis)
    {
        timer.update(elapsedMillis);
        if(hasExpired())
        {
            factory.createExplosion(coll.getCenterOfMass(), EXPLOSION_RADIUS, EXPLOSION_DAMAGE, explosionAlpha);
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

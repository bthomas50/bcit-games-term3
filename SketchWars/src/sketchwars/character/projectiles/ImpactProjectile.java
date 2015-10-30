package sketchwars.character.projectiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import sketchwars.graphics.Texture;
import sketchwars.character.*;
import sketchwars.map.AbstractMap;
import sketchwars.physics.*;
import sketchwars.util.Timer;
/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class ImpactProjectile extends AbstractProjectile {
    private static final int EXPLOSION_DAMAGE = 15;
    public static final double EXPLOSION_RADIUS = 75.0;
    
    public static final double EXPIRE_MILLI = 15000.0;
    public static final double ACTIVATE_MILLI = 150.0;
    
    private final ProjectileFactory factory;
    private final BufferedImage explosionAlpha;
    
    private short explodeCall;
    private final Timer expirationTimer;
    private final Timer activationTimer;
    
    private boolean activate;
    
    public ImpactProjectile(Texture texture, ProjectileFactory factory) {
        super(texture, null, 0);
        this.factory = factory;
        
        int radius = (int)EXPLOSION_RADIUS;
        explosionAlpha = new BufferedImage(radius*2, radius*2, BufferedImage.TYPE_INT_ARGB);
        Graphics g = explosionAlpha.getGraphics();
        g.fillOval(0, 0, radius*2, radius*2);
        
        expirationTimer = new Timer(EXPIRE_MILLI);
        expirationTimer.start();
        
        activationTimer = new Timer(ACTIVATE_MILLI);
        activationTimer.start();
    }

    @Override
    public void update(double elapsedMillis)
    {
        if (activate && hasExpired() && explodeCall == 0) {
            factory.createExplosion(coll.getCenterOfMass(), EXPLOSION_RADIUS, EXPLOSION_DAMAGE, explosionAlpha);
            explodeCall++;
        }
        
        expirationTimer.update(elapsedMillis);
        activationTimer.update(elapsedMillis);
        
        if (activationTimer.hasElapsed()) {
            activate = true;
        }
    }

    @Override 
    public boolean hasExpired()
    {
        return expired || expirationTimer.hasElapsed();
    }
    
    @Override
    protected void handleCollisionWithCharacter(SketchCharacter ch) { 
        if (activate && ch != null) {
             expired = true;
        }
    }
    
    @Override
    protected void handleCollision(Collider c) { 
        if (activate && c != null && c.hasAttachedGameObject() &&
                c.getAttachedGameObject() instanceof AbstractMap) {
             expired = true;
        }
    }
}

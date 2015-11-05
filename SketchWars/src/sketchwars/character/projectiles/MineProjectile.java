package sketchwars.character.projectiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import org.joml.Vector2d;
import sketchwars.SketchWars;
import sketchwars.animation.Animation;
import sketchwars.graphics.Texture;
import sketchwars.character.*;
import sketchwars.physics.*;
import sketchwars.util.Converter;
import sketchwars.util.Timer;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class MineProjectile extends AbstractProjectile {
    private static final int EXPLOSION_DAMAGE = 35;
    public static final double EXPLOSION_RADIUS = 100.0;
    
    private static final double ACTIVATE_AFTER_MILLIS = 2000;
    private final ProjectileFactory factory;
    private final BufferedImage explosionAlpha;
    
    private final Animation activatedTex;
    private boolean activated;
    private short explodeCall;
    private final Timer timer;
    
    public MineProjectile(Texture texture, Animation activatedTex, ProjectileFactory factory) {
        super(texture, null, 0);
        this.factory = factory;
        this.activatedTex = activatedTex;
        
        int radius = (int)EXPLOSION_RADIUS;
        explosionAlpha = new BufferedImage(radius*2, radius*2, BufferedImage.TYPE_INT_ARGB);
        Graphics g = explosionAlpha.getGraphics();
        g.fillOval(0, 0, radius*2, radius*2);
        
        timer = new Timer(ACTIVATE_AFTER_MILLIS);
        timer.start();
    }

    @Override
    public void update(double elapsedMillis)
    {
        if (hasExpired() && explodeCall == 0) {
            factory.createExplosion(coll.getCenterOfMass(), EXPLOSION_RADIUS, EXPLOSION_DAMAGE, explosionAlpha);
            explodeCall++;
        }
        
        timer.update(elapsedMillis);
        
        if (timer.hasElapsed()) {
            activated = true;
        }
        
        if (activatedTex != null && activated) {
            activatedTex.update(elapsedMillis);
        }
    }

    @Override
    public void render() {
        if (activatedTex != null && activated) {
            BoundingBox bounds = coll.getBounds();
            long vCenter = bounds.getCenterVector();
            
            Vector2d position = Converter.PhysicsToGraphics(Vectors.xComp(vCenter), Vectors.yComp(vCenter));
            Vector2d size = Converter.PhysicsToGraphics(bounds.getWidth(), bounds.getHeight());
            
            activatedTex.setPosition(position);
            activatedTex.setDimension(size);
            activatedTex.render();
        } else {
            super.render(); 
        }
    }

    
    @Override 
    public boolean hasExpired()
    {
        return expired;
    }
    
    @Override
    protected void handleCollisionWithCharacter(SketchCharacter ch) {
        if (ch != null && activated) {
            expired = true;
        }
    }
    
    @Override
    protected void handleCollision(Collider c) {
        if (c != null && c.hasAttachedGameObject()) {
            if (c.getAttachedGameObject() instanceof AbstractProjectile) {
                expired = true;
            }
        }
    }
}

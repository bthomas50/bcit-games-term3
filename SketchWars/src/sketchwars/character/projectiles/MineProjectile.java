package sketchwars.character.projectiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import org.joml.Vector2d;
import sketchwars.animation.Animation;
import sketchwars.graphics.Texture;
import sketchwars.character.*;
import sketchwars.physics.*;
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
        if (hasExpired() && explodeCall == 1) {
            factory.createExplosion(coll.getCenterOfMass(), EXPLOSION_RADIUS, EXPLOSION_DAMAGE, explosionAlpha);
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
            
            Vector2d position = new Vector2d(Vectors.xComp(vCenter) / 1024.0f, Vectors.yComp(vCenter) / 1024.0f);
            Vector2d size = new Vector2d(bounds.getWidth() / 1024.0f, bounds.getHeight() / 1024.0f);
            
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
            explodeCall++;
            expired = true;
        }
    }
    
    @Override
    protected void handleCollision(Collider c) { }
}

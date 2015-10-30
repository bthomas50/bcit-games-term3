package sketchwars.character.projectiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import org.joml.Matrix3d;
import sketchwars.animation.Animation;
import sketchwars.character.SketchCharacter;

import sketchwars.graphics.Texture;
import sketchwars.physics.*;
/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class BazookaProjectile extends AbstractProjectile {
    public static final double RANGE = 500;
    private static final int EXPLOSION_DAMAGE = 45;
    public static final double EXPLOSION_RADIUS = 150.0;
    
    private short explodeCall;
    private final ProjectileFactory factory;
    private final BufferedImage explosionAlpha;
    
    private final Animation flame;
    
    public BazookaProjectile(Texture texture, Animation flame, ProjectileFactory factory) {
        super(texture, null, 0);
        this.factory = factory;
        this.flame = flame;
        
        int radius = (int)EXPLOSION_RADIUS;
        explosionAlpha = new BufferedImage(radius*2, radius*2, BufferedImage.TYPE_INT_ARGB);
        Graphics g = explosionAlpha.getGraphics();
        g.fillOval(0, 0, radius*2, radius*2);
    }

    @Override
    public void render() {  
        super.render();
        
        if (flame != null) {
                Matrix3d matrix = new Matrix3d();
                
                BoundingBox bounds = coll.getBounds();
                long vCenter = bounds.getCenterVector();
                long velocity = Vectors.normalize(coll.getVelocity());
                double distance = 0.04 * 1024;
                long flamePos = Vectors.subtract(vCenter, Vectors.scalarMultiply(distance, velocity));
                
                matrix.translation(Vectors.xComp(flamePos) / 1024.0f, Vectors.yComp(flamePos) / 1024.0f);
                
                float angle = (float)Math.atan2(Vectors.yComp(velocity), Vectors.xComp(velocity));
                matrix.rotate(angle, 0, 0, 1);
                
                matrix.scale(bounds.getWidth() / 1024.0f, bounds.getHeight() / 1024.0f, 1);
                
                flame.setTransform(matrix, true);
                flame.render();
            }
    }

    @Override
    public void update(double elapsedMillis) {
        super.update(elapsedMillis); 
        
        if(hasExpired() && explodeCall == 0)
        {
            factory.createExplosion(coll.getCenterOfMass(), EXPLOSION_RADIUS, EXPLOSION_DAMAGE, explosionAlpha);
            explodeCall++;
        }
        
        if (flame != null) {
            flame.update(elapsedMillis);
        }
    }
        
    @Override
    protected void handleCollisionWithCharacter(SketchCharacter ch) { }

    @Override
    protected void handleCollision(Collider c) {
        if(c != null) {
            expired = true;
        }
    }
}

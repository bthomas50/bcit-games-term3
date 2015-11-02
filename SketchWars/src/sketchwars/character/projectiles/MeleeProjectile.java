package sketchwars.character.projectiles;

import sketchwars.graphics.Texture;
import sketchwars.character.SketchCharacter;
import sketchwars.util.Timer;
import sketchwars.physics.*;

import java.util.HashSet;
import org.joml.Matrix3d;
/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class MeleeProjectile extends AbstractProjectile {
    private static final int DAMAGE = 20;
    public static final double COLLIDER_RADIUS = 36.0;
    private static final double LIFESPAN = 200;
    private HashSet<SketchCharacter> damagedChars;
    private HashSet<Collider> pushedObjects;
    private Timer timer;
    
    private boolean attackLeft;
    
    public MeleeProjectile(Texture texture, SketchCharacter owner, long vVelocity) {
        super(texture, owner, DAMAGE);
        timer = new Timer(LIFESPAN);
        timer.start();
        damagedChars = new HashSet<>();
        pushedObjects = new HashSet<>();
        
        attackLeft = (Vectors.xComp(vVelocity) <= 0);
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

    @Override
    protected void handleCollisionWithCharacter(SketchCharacter ch) {
        if(!ch.equals(owner) && !damagedChars.contains(ch)) {
            ch.takeDamage(damage);
            damagedChars.add(ch);
            System.out.println(ch + " is hit for " + damage + " damage.");
        }
    }

    @Override
    public void render() {
        if (texture != null) {
            BoundingBox bounds = coll.getBounds();
            long vCenter = bounds.getCenterVector();
            float posX = (float) (Vectors.xComp(vCenter) / 1024.0f);
            float posY = (float) (Vectors.yComp(vCenter) / 1024.0f);
            float sizeX = (float) (bounds.getWidth() / 1024.0f);
            float sizeY = (float) (bounds.getHeight() / 1024.0f);
            
            Matrix3d tranform = new Matrix3d();
            Matrix3d scale = new Matrix3d();
            
            float xOffset = 0.02f;
            
            if (attackLeft) {
                tranform.translation(posX - xOffset + sizeX, posY + Y_OFFSET);
                scale.scaling(-sizeX, sizeY, 1);
            } else {
                tranform.translation(posX - xOffset, posY + Y_OFFSET);
                scale.scaling(sizeX, sizeY, 1);
            }
            
            tranform.mul(scale);
            
            texture.draw(tranform);
        }
    }

    @Override
    protected void handleCollision(Collider c) {
        if(!pushedObjects.contains(c))
        {
            c.applyForce(Vectors.create(4000 * getXDir(), 2000), 1000.0);
            pushedObjects.add(c);
        }
    }

    private int getXDir()
    {
        return attackLeft ? -1 : 1;
    }
}

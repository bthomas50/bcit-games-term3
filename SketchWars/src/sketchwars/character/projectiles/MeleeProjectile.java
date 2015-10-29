package sketchwars.character.projectiles;

import sketchwars.graphics.Texture;
import sketchwars.character.SketchCharacter;
import sketchwars.util.Timer;
import sketchwars.physics.*;

import java.util.HashSet;
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
    private int xDir;
    
    public MeleeProjectile(Texture texture, SketchCharacter owner, int directionX) {
        super(texture, owner, DAMAGE);
        timer = new Timer(LIFESPAN);
        timer.start();
        damagedChars = new HashSet<>();
        pushedObjects = new HashSet<>();
        xDir = directionX;
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
    protected void handleCollision(Collider c) {
        if(!pushedObjects.contains(c))
        {
            c.applyForce(Vectors.create(1000 * xDir, 200), 1000.0);
            pushedObjects.add(c);
        }
    }
}

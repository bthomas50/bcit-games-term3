package sketchwars.character.projectiles;

import sketchwars.graphics.Texture;
import sketchwars.character.SketchCharacter;
import sketchwars.util.Timer;
import sketchwars.physics.*;
/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class MeleeProjectile extends AbstractProjectile {
    private static final int DAMAGE = 20;
    public static final double COLLIDER_RADIUS = 36.0;
    private static final double LIFESPAN = 200;
    
    private Timer timer;
    
    public MeleeProjectile(Texture texture, SketchCharacter owner) {
        super(texture, owner, DAMAGE);

        timer = new Timer(LIFESPAN);
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

    @Override
    protected void handleCollisionWithCharacter(SketchCharacter ch) {
        if(!ch.equals(owner)) {
            ch.takeDamage(damage);
            System.out.println(ch + " is hit for " + damage + " damage.");
        }
    }

    @Override
    protected void handleCollision(Collider c) {}
}

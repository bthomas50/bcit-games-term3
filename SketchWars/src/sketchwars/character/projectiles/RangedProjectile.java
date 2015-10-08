package sketchwars.character.projectiles;

import sketchwars.graphics.Texture;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class RangedProjectile extends BasicProjectile {
    private static final int DAMAGE = 35;
    private static final double RANGE = 200;
    private static final double LIFESPAN = 10;
    
    private double projectileRange;
    
    public RangedProjectile(Texture texture) {
        super(texture);
        
        setLifespan(LIFESPAN);
        
        projectileRange = RANGE;
        setDamage(DAMAGE);
    }

    public double getProjectileRange() {
        return projectileRange;
    }

    public void setProjectileRange(double projectileRange) {
        this.projectileRange = projectileRange;
    }
}

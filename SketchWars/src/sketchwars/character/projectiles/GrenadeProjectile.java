package sketchwars.character.projectiles;

import sketchwars.graphics.Texture;
/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class GrenadeProjectile extends BasicProjectile {
    private static final int DAMAGE = 25;
    private static final double EXPLOSION_RADIUS = 512.0;
    
    private static final double LIFESPAN = 5000;
    
    private double explosionRadius;
    
    public GrenadeProjectile(Texture texture, double scale) {
        super(texture, scale);
        
        setLifespan(LIFESPAN);
        
        explosionRadius = EXPLOSION_RADIUS;
        setDamage(DAMAGE);
    }

    public double getExplosionRadius() {
        return explosionRadius;
    }

    public void setExplosionRadius(double explosionRadius) {
        this.explosionRadius = explosionRadius;
    }
}

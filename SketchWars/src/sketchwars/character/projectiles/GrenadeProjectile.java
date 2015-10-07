package sketchwars.character.projectiles;

import sketchwars.graphics.Texture;
/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class GrenadeProjectile extends BasicProjectile {
    private static final int DAMAGE = 25;
    private static final double COLLIDER_RADIUS = 32.0;
    private static final double EXPLOSION_RADIUS = 256.0;
    
    private static final double LIFESPAN = 5000;
    
    private double colliderRadius;
    private double explosionRadius;
    
    public GrenadeProjectile(Texture texture) {
        super(texture);
        
        setLifespan(LIFESPAN);
        
        colliderRadius = COLLIDER_RADIUS;
        explosionRadius = EXPLOSION_RADIUS;
        setDamage(DAMAGE);
    }

    public double getColliderRadius() {
        return colliderRadius;
    }

    public void setColliderRadius(double colliderRadius) {
        this.colliderRadius = colliderRadius;
    }

    public double getExplosionRadius() {
        return explosionRadius;
    }

    public void setExplosionRadius(double explosionRadius) {
        this.explosionRadius = explosionRadius;
    }
}

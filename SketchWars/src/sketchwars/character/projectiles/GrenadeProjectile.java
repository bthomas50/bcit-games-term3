package sketchwars.character.projectiles;

import sketchwars.graphics.Texture;
/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class GrenadeProjectile extends BasicProjectile {
    private static final int DAMAGE = 25;
    private static final float EXPLOSION_RADIUS = 0.36f;
    
    private static final double LIFESPAN = 5000;
    
    public GrenadeProjectile(Texture texture) {
        super(texture);
        
        setLifespan(LIFESPAN);
        
        setDamage(DAMAGE);
    }

    
    public float getExplosionRadius() {
        return EXPLOSION_RADIUS;
    }

}

package sketchwars.character.projectiles;

import sketchwars.OpenGL;
import sketchwars.graphics.Texture;
/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class GrenadeProjectile extends BasicProjectile {
    private static final int DAMAGE = 25;
    private static final double EXPLOSION_RADIUS = 0.00012;
    
    private static final double LIFESPAN = 5000;
    
    public GrenadeProjectile(Texture texture) {
        super(texture);
        
        setLifespan(LIFESPAN);
        
        setDamage(DAMAGE);
    }

    
    public double getExplosionRadius() {
        return EXPLOSION_RADIUS * OpenGL.WIDTH;
    }

}

package sketchwars.character.projectiles;

import sketchwars.graphics.Texture;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class MeleeProjectile extends BasicProjectile {
    private static final int DAMAGE = 20;
    private static final double OBJECT_RADIUS = 36.0;
    private static final double LIFESPAN = 200;
    
    public MeleeProjectile(Texture texture, double scale) {
        super(texture, scale);
        
        setLifespan(LIFESPAN);
        setDamage(DAMAGE);
    }

}

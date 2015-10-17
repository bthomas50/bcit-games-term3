package sketchwars.character.projectiles;

import sketchwars.graphics.Texture;
import sketchwars.character.SketchCharacter;
/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class MeleeProjectile extends BasicProjectile {
    private static final int DAMAGE = 20;
    public static final double COLLIDER_RADIUS = 36.0;
    public static final double LIFESPAN = 200;
    
    
    public MeleeProjectile(Texture texture, SketchCharacter owner) {
        super(texture, owner, DAMAGE);
    }
}

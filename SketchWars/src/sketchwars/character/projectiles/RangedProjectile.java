package sketchwars.character.projectiles;

import sketchwars.graphics.Texture;
import sketchwars.character.SketchCharacter;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class RangedProjectile extends BasicProjectile {
    private static final int DAMAGE = 35;
    public static final double RANGE = 1000;
    
    private double projectileRange;
    
    public RangedProjectile(Texture texture, SketchCharacter owner) {
        super(texture, owner, DAMAGE);
    }
}

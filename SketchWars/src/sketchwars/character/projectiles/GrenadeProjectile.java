package sketchwars.character.projectiles;

import sketchwars.graphics.Texture;
/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class GrenadeProjectile extends AbstractProjectile {

    public GrenadeProjectile(Texture texture) {
        super(texture);
        
        setMass(1);
        setElasticity(0.9f);
    }

    
}

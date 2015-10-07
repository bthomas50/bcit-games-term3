package sketchwars.character.weapon;

import sketchwars.character.SketchCharacter;
import sketchwars.character.projectiles.*;
import sketchwars.graphics.Texture;
import sketchwars.sound.SoundPlayer;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class RangedWeapon extends AbstractWeapon {

    public RangedWeapon(Texture texture, ProjectileFactory projectileFactory) {
        this(texture, 1, projectileFactory);
    }
    
    public RangedWeapon(Texture texture, double scale, ProjectileFactory projectileFactory) {
        super(texture, scale, projectileFactory);       
        init();
    }

    private void init() {
        setRateOfFire(10);
    }
    
    @Override
    public BasicProjectile createProjectile(SketchCharacter owner, long vPosition, long vVelocity) {
        try{
            SoundPlayer.playSFX(1, true, 0);
        } catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        return  projectileFactory.createRanged(owner, vPosition, vVelocity, scale);
    }

    @Override
    protected double getProjectileSpeed(float power) {
        return power * 1000.0f;
    }

}

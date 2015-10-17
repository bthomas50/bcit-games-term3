package sketchwars.character.weapon;

import sketchwars.character.SketchCharacter;
import sketchwars.character.projectiles.*;
import sketchwars.graphics.Texture;
import sketchwars.sound.SoundPlayer;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class MeleeWeapon extends AbstractWeapon {
    public MeleeWeapon(Texture texture, ProjectileFactory projectileFactory) {
        this(texture, 1, projectileFactory);
    }
    
    public MeleeWeapon(Texture texture, double scale, ProjectileFactory projectileFactory) {
        super(texture, scale, projectileFactory);
        setRateOfFire(0.5f);
    }
    
    @Override
    public void render() {
        if (texture != null) {
            texture.drawNormalized(posX, posY, scale);
        }
    }

    @Override
    public AbstractProjectile createProjectile(SketchCharacter owner, long vPosition, long vVelocity) {
        try{
            SoundPlayer.playSFX(0, true, 0);
        } catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        return projectileFactory.createMelee(owner, vPosition, vVelocity, scale);
    }

    @Override
    protected double getProjectileSpeed(float power) {
        return 10.0f;
    }

}

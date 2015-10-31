package sketchwars.character.weapon;

import sketchwars.character.SketchCharacter;
import sketchwars.character.projectiles.*;
import sketchwars.graphics.Texture;
import sketchwars.sound.SoundPlayer;
import sketchwars.physics.Vectors;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class MeleeWeapon extends AbstractWeapon {
    public MeleeWeapon(Texture texture, float width, float height, ProjectileFactory projectileFactory) {
        super(texture, width, height, projectileFactory);
        setRateOfFire(0.5f);
    }

    @Override
    protected long getFireDirection(long vAimDirection) {
        if(Vectors.xComp(vAimDirection) < 0) {
            return Vectors.V_UNIT_X;
        } else {
            return Vectors.reverse(Vectors.V_UNIT_X);
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
        
        return projectileFactory.createMelee(owner, vPosition, vVelocity);
    }

    @Override
    protected double getProjectileSpeed(float power) {
        return 1.0f;
    }

}

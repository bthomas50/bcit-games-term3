package sketchwars.character.weapon;

import sketchwars.character.SketchCharacter;
import sketchwars.character.projectiles.*;
import sketchwars.graphics.Texture;
import sketchwars.physics.Vectors;
import sketchwars.sound.SoundPlayer;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class RangedWeapon extends AbstractWeapon {
    private static final double VELOCITY = 5;
    
    public RangedWeapon(Texture texture, float width, float height, ProjectileFactory projectileFactory) {
        super(texture, width, height, projectileFactory);     
        init();
    }

    private void init() {
        setRateOfFire(10);
    }
    
    @Override
    public AbstractProjectile createProjectile(SketchCharacter owner, long vPosition, long vVelocity) {
        try{
            SoundPlayer.playSFX(1, true, 0);
        } catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        long newVelocity = Vectors.scalarMultiply(VELOCITY, vVelocity);
        return  projectileFactory.createRanged(owner, vPosition, newVelocity);
    }

    @Override
    protected double getProjectileSpeed(float power) {
        return power * 1000.0f;
    }

}

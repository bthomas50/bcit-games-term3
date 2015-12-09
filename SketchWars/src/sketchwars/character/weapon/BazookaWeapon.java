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
public class BazookaWeapon extends AbstractWeapon {
    private static final double VELOCITY = 0.45;
    public BazookaWeapon(Texture texture, float width, float height, ProjectileFactory projectileFactory) {
        super(texture, width, height, projectileFactory);     
        init();
    }

    private void init() {
        setRateOfFire(0.5f);
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
        return projectileFactory.createBazookaRocket(owner, vPosition, newVelocity);
    }

    @Override
    protected double getProjectileSpeed(float power) {
        return power * 3000.0f;
    }

}

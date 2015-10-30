package sketchwars.character.weapon;

import sketchwars.character.SketchCharacter;
import sketchwars.character.projectiles.*;
import sketchwars.graphics.Texture;
import sketchwars.sound.SoundPlayer;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class MineWeapon extends AbstractWeapon {   
    private double explosionRadius;
    
    public MineWeapon(Texture texture, float width, float height, ProjectileFactory projectileFactory) {
        super(texture, width, height, projectileFactory);
        init();
    }
    
    private void init() {
        explosionRadius = 1;
        setRateOfFire(0.5f);
    }

    public double getExplosionRadius() {
        return explosionRadius;
    }

    public void setExplosionRadius(double explosionRadius) {
        this.explosionRadius = explosionRadius;
    }

    @Override
    protected AbstractProjectile createProjectile(SketchCharacter owner, long vPosition, long vVelocity) {
        try{
            SoundPlayer.playSFX(2, true, 0);
        } catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        return projectileFactory.createMine(owner, vPosition, vVelocity);
    }

    @Override
    protected double getProjectileSpeed(float power) {
        return power * 500.0f;
    }

}

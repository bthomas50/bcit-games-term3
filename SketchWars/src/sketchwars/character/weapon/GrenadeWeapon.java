/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.character.weapon;

import sketchwars.character.projectiles.AbstractProjectile;
import sketchwars.character.projectiles.GrenadeProjectile;
import sketchwars.graphics.Texture;
import sketchwars.physics.BitMaskFactory;
import sketchwars.physics.PixelCollider;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class GrenadeWeapon extends AbstractWeapon {   
    private double explosionRadius;    
    private Texture grenadeTexture;
    
    public GrenadeWeapon(Texture texture) {
        super(texture, 1);
        init();
    }
    
    public GrenadeWeapon(Texture texture, double scale) {
        super(texture, scale);
        init();
    }
    
    private void init() {
        explosionRadius = 1;
        
        setRateOfFire(0.5f);
        
        grenadeTexture = new Texture();
        grenadeTexture.loadTexture("content/char/weapons/grenade.png");
    }

    public double getExplosionRadius() {
        return explosionRadius;
    }

    public void setExplosionRadius(double explosionRadius) {
        this.explosionRadius = explosionRadius;
    }

    @Override
    protected PixelCollider createProjectilePixelCollider() {
        return new PixelCollider(BitMaskFactory.createCircle(32));
    }

    @Override
    protected AbstractProjectile getProjectile() {
        return new GrenadeProjectile(grenadeTexture);
    }
}

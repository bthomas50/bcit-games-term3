/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.character.weapon;

import sketchwars.character.projectiles.AbstractProjectile;
import sketchwars.character.projectiles.RangedProjectile;
import sketchwars.graphics.Texture;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class RangedWeapon extends AbstractWeapon {
    private Texture bulletTexture;

    public RangedWeapon(Texture texture) {
        super(texture, 1);        
        init();
    }
    
    public RangedWeapon(Texture texture, double scale) {
        super(texture, scale);       
        init();
    }

    private void init() {
        setRateOfFire(2);
        
        bulletTexture = new Texture();
        bulletTexture.loadTexture("content/char/weapons/bullet1.png");
    }
    
    @Override
    protected AbstractProjectile getProjectile() {
        RangedProjectile projectile = new RangedProjectile(bulletTexture);
        
        return projectile;
    }
}

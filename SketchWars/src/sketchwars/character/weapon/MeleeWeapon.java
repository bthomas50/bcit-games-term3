/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.character.weapon;

import sketchwars.character.projectiles.AbstractProjectile;
import sketchwars.character.projectiles.MeleeProjectile;
import sketchwars.graphics.Texture;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class MeleeWeapon extends AbstractWeapon {
    public MeleeWeapon(Texture texture) {
        super(texture, 1);
        
        setRateOfFire(0.5f);
    }
    
    public MeleeWeapon(Texture texture, double scale) {
        super(texture, scale);
    }
    
    @Override
    public void render() {
        if (texture != null) {
            texture.drawNormalized(posX, posY, scale);
        }
    }

    @Override
    protected AbstractProjectile getProjectile() {
        return new MeleeProjectile(null);
    }
}

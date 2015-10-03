/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.character.weapon;

import sketchwars.graphics.Texture;
import sketchwars.physics.Vectors;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class GrenadeWeapon extends AbstractWeapon {

    public GrenadeWeapon(Texture texture) {
        super(texture, 1);
    }
    
    public GrenadeWeapon(Texture texture, double scale) {
        super(texture, scale);
    }

    @Override
    public void fire(float power, long direction) {
        
    }
}

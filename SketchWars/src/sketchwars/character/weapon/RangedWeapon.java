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
public class RangedWeapon extends AbstractWeapon {

    public RangedWeapon(Texture texture) {
        super(texture, 1);
    }
    
    public RangedWeapon(Texture texture, double scale) {
        super(texture, scale);
    }
    
    
    public void fire(float power, Vectors direction) {
        
    }
    
}

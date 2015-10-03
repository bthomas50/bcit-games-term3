/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.character.weapon;

import sketchwars.graphics.Texture;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class MeleeWeapon extends AbstractWeapon {
    private int direction;
    
    public MeleeWeapon(Texture texture) {
        super(texture, 1);
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

    /**
     * Set melee weapon render direction
     * @param direction Negative value is left, positive value is right 
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }
    
    public void fire() {
        
    }

}

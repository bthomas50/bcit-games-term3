/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.character.projectiles;

import sketchwars.graphics.Texture;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class MeleeProjectile extends AbstractProjectile {

    public MeleeProjectile(Texture texture) {
        super(texture);
        
        setMass(1);
        setElasticity(0);
    }

}

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
public class TestBullet extends AbstractProjectile {

    @Override
    public void init() {
        texture = new Texture();
        texture.loadTexture("content/char/projectiles/testB.png");
    }
    
}

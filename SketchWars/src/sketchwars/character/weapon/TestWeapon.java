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
public class TestWeapon extends AbstractWeapon {
    @Override
    public void init() {
        texture = new Texture();
        texture.loadTexture("content/char/weapons/test.png");
    }
}

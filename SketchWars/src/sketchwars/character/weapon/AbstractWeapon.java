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
public abstract class AbstractWeapon {
    Texture texture;
    public abstract void init();
    
    public void render(double playerX, double playerY) {
        if (texture != null) {
            texture.drawNormalizedPosition(playerY + 0.1f, playerY, texture.getTextureWidth(), texture.getTextureHeight());
        }
    }
    
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}

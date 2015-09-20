/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.character;

import org.lwjgl.opengl.GL13;
import sketchwars.character.weapon.AbstractWeapon;
import sketchwars.graphics.GraphicsObject;
import sketchwars.graphics.Texture;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class Character implements GraphicsObject {
    Texture texture;
    AbstractWeapon weapon;

    public Character() {
       
    }
    
    public void init() {
        texture = new Texture();
        texture.loadTexture("content/char/char.png", GL13.GL_TEXTURE0);
    }
    
    public void setWeapon(AbstractWeapon weapon) {
        this.weapon = weapon;
    }
    
    @Override
    public void render() {
        texture.drawNormalized(0, 0, 0.1, 0.1);
        
        if (weapon != null) {
            weapon.render(0, 0);
        }
    }
    
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}

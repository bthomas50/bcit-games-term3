/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.graphics.map;

import org.lwjgl.opengl.GL13;
import sketchwars.graphics.Drawable;
import sketchwars.graphics.Texture;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class TestMapClass implements Drawable {
    Texture texture;
    
    public  TestMapClass() {
       texture = new Texture();
       texture.loadTexture("content/map/test.png", GL13.GL_TEXTURE0);
    }
    
    @Override
    public void render() {
        texture.draw(0, 0, 250, 250);
    }
    
}

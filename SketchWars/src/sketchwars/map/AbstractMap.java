/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.map;

import sketchwars.graphics.GraphicsObject;
import sketchwars.graphics.Texture;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public abstract class AbstractMap implements GraphicsObject {
    Texture texture;
    
    public abstract void init();
    
    @Override
    public void render() {
        if (texture != null) {
            texture.drawNormalized(0, -0.5, 0.90, 0.5);
        }
    }
    
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}

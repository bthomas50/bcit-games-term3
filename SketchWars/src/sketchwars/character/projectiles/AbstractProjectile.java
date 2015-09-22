/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.character.projectiles;

import sketchwars.graphics.GraphicsObject;
import sketchwars.graphics.Texture;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public abstract class AbstractProjectile implements GraphicsObject {
    private double posX;
    private double posY;
    
    Texture texture;
        
    public abstract void init();
    
    public void setPosition(double x, double y) {
        this.posX = x;
        this.posY = y;
    }
    
    @Override
    public void render() {
        if (texture != null) {
            texture.drawNormalized(posX, posY, 1);
        }
    }
    
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}

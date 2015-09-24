/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.character.projectiles;

import com.sun.javafx.geom.Matrix3f;
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
           /* Matrix3f matrix = new Matrix3f(2, 0, (float)posX, 0, 1, (float)posY, 0, 0, 1);
            texture.draw(matrix);*/
        }
    }
    
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}

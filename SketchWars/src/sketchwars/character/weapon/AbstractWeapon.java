/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.character.weapon;

import sketchwars.GameObject;
import sketchwars.graphics.GraphicsObject;
import sketchwars.graphics.Texture;
import sketchwars.physics.Vectors;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public abstract class AbstractWeapon implements GameObject, GraphicsObject {
    protected double posX;
    protected double posY;
    protected double scale;
    protected Texture texture;

    public AbstractWeapon(Texture texture, double scale) {
        this.texture = texture;
        this.scale = scale;
    }
    
    @Override
    public void render() {
        if (texture != null) {
            texture.drawNormalized(posX, posY, scale);
        }
    }
    
    @Override
    public void update(double elapsed) {
        
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosition(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }
    
    public abstract void fire(float power, long direction);
}

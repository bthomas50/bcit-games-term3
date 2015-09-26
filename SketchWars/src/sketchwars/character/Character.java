/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.character;

import sketchwars.physics.*;
import sketchwars.character.weapon.AbstractWeapon;
import sketchwars.graphics.*;
import sketchwars.GameObject;
/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class Character implements GraphicsObject, GameObject {
    private Texture texture;
    private AbstractWeapon weapon;
    private Collider coll;

    public Character() {
        coll = new PixelCollider(BitMaskFactory.createRectangle(1, 1));
    }
    
    public void init() {
        texture = new Texture();
        texture.loadTexture("content/char/char.png");
    }
    
    public void setCollider(Collider coll) {
        this.coll = coll;
    }

    public void setWeapon(AbstractWeapon weapon) {
        this.weapon = weapon;
    }
    
    @Override
    public void update(double elapsedMillis) {

    }

    @Override
    public void render() {
        BoundingBox bounds = coll.getBounds();

        texture.drawNormalized((double)bounds.getLeft() / 1024.0, (double) bounds.getTop() / 1024.0, (double) bounds.getWidth() / 2048.0, (double) bounds.getHeight() / 2048.0);
        
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

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
    public static final int DEFAULT_MAX_HEALTH = 100;
    
    private double posX;
    private double posY;
    private double width;
    private double height;
        
    private Texture texture;
    private AbstractWeapon weapon;
    private Collider coll;
    private int maxHealth;
    private int health;
    private boolean isDead;

    public Character(Texture texture) {
        coll = new PixelCollider(BitMaskFactory.createRectangle(1, 1));
        
        this.texture = texture;
        this.maxHealth = DEFAULT_MAX_HEALTH;
        this.health = DEFAULT_MAX_HEALTH;
        this.isDead = false;
    }
    
    public Character(Texture texture, int maxHealth, int health) {
        coll = new PixelCollider(BitMaskFactory.createRectangle(1, 1));
        
        this.texture = texture;
        this.maxHealth = maxHealth;
        this.health = health;
        this.isDead = false;
    }
    
    public void setCollider(Collider coll) {
        this.coll = coll;
    }

    public void setWeapon(AbstractWeapon weapon) {
        this.weapon = weapon;
    }
    
    @Override
    public void update(double delta) {
        updateCharacterInfo();
        
        if (weapon != null) {
            weapon.setPosition(posX + 0.01, posY - 0.01);
            weapon.update(delta);
        }
    }

    private void updateCharacterInfo() {
        BoundingBox bounds = coll.getBounds();
        long vCenter = bounds.getCenterVector();
        posX = Vectors.xComp(vCenter) / 1024.0;
        posY = Vectors.yComp(vCenter) / 1024.0;
        
        width = (double) bounds.getWidth() / 2048.0;
        height = (double) bounds.getHeight() / 2048.0;
    }

    @Override
    public void render() {
        texture.drawNormalized(posX , posY, width, height);
        
        if (weapon != null) {
            weapon.render();
        }
    }
    
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
    
    public int getHealth() {
        return health;
    }
    
    public int setHealth(int value) {
        this.health = value;
        
        return this.health;
    }
    
    public int getMaxHealth() {
        return maxHealth;
    }
    
    public int  setMaxHealth(int value) {
        this.maxHealth = value;
        return maxHealth;
    }
    
    public int takeDamage(int value) {
        this.health -= value;
        
        if (health < 1) {
            health = 0;
            isDead = true;
        }
        
        return this.health;
    }
    
    public int heal(int value) {
        this.health += value;
        
        if (health > maxHealth) {
            health = maxHealth;
        }
        
        return this.health;
    }
    
    public boolean isDead() {
        return isDead;
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

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public AbstractWeapon getWeapon() {
        return weapon;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}

package sketchwars.character;

import org.joml.Matrix3d;
import sketchwars.physics.*;
import sketchwars.character.weapon.AbstractWeapon;
import sketchwars.graphics.*;
import sketchwars.game.GameObject;
import sketchwars.input.Command;
import sketchwars.input.Input;
import static sketchwars.physics.Vectors.create;
/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class SketchCharacter implements GraphicsObject, GameObject {
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
    
    private boolean hasFired;
    private boolean isFacingLeft;
    private double angle;

    private Texture reticleTexture;

    public SketchCharacter(Texture texture) {
        this(texture, DEFAULT_MAX_HEALTH, DEFAULT_MAX_HEALTH);
    }
    
    public SketchCharacter(Texture texture, int maxHealth, int health) {
        coll = new PixelCollider(BitMaskFactory.createRectangle(1, 1));
        
        this.texture = texture;
        this.maxHealth = maxHealth;
        this.health = health;
        this.isDead = false;
        this.hasFired = false;
        this.angle = 0.0;
        this.isFacingLeft = false;//start facing right.
        reticleTexture = Texture.loadTexture("content/misc/reticle.png");
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
        
        if(health <= 0)
            isDead = true;
    }

    @Override
    public boolean hasExpired() {
        return false;
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
        texture.drawNormalized(posX, posY, width, height);
        
        if (weapon != null) {
            weapon.render();
            long vReticleOffset = Vectors.createRTheta(0.1, getActualFireAngle());
            reticleTexture.drawNormalized(posX + Vectors.xComp(vReticleOffset), posY + Vectors.yComp(vReticleOffset), 0.05, 0.05);
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
    
    public int setMaxHealth(int value) {
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

    public void resetHasFiredThisTurn() {
        hasFired = false;
    }

    public boolean hasFiredThisTurn() {
        return hasFired;
    }

    public void fireCurrentWeapon(double power) {
        if(weapon != null) {
            hasFired = weapon.tryToFire(this, (float)power, Vectors.createRTheta(1.0f, getActualFireAngle()));
        }
    }

    public void aimUp(double elapsedMillis) {
        angle += Math.PI * elapsedMillis / 1000.0;
        //make sure not to aim higher than straight up
        angle = Math.min(angle, Math.PI / 2.0);
        System.out.println("angle: " + angle);
    }

    public void aimDown(double elapsedMillis) {
        angle -= Math.PI * elapsedMillis / 1000.0;
        //make sure not to aim lower than straight down
        angle = Math.max(angle, -Math.PI / 2.0);
        System.out.println("angle: " + angle);
    }

    void moveLeft(double elapsedMillis) 
    {
        long oldVector = coll.getVelocity();
        this.isFacingLeft = true;
        double getY = Vectors.yComp(oldVector);
        coll.setVelocity(create(-100, getY));
    }

    void moveRight(double elapsedMillis)
    {
        long oldVector = coll.getVelocity();
        this.isFacingLeft = false;
        double getY = Vectors.yComp(oldVector);
        coll.setVelocity(create(100, getY));
    }
    
    void jump(double elapsedMillis)
    {
        long oldVector = coll.getVelocity();
        double getX = Vectors.xComp(oldVector);
        coll.setVelocity(create(getX, 200));
    }

    private double getActualFireAngle() {
        if(isFacingLeft) {
            return Math.PI - angle;
        } else {
            return angle;
        }
    }

    public Collider getCollider() {
        return coll;
    }
}

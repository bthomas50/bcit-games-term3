package sketchwars.character;

import org.joml.Vector2d;
import sketchwars.animation.AnimationSet;
import sketchwars.animation.CharacterAnimations;
import sketchwars.physics.*;
import sketchwars.character.weapon.AbstractWeapon;
import sketchwars.graphics.*;
import sketchwars.game.GameObject;
import sketchwars.HUD.HealthBar;
import sketchwars.SketchWars;
import sketchwars.character.projectiles.AbstractProjectile;
import sketchwars.character.projectiles.MineProjectile;
import sketchwars.input.MouseHandler;
import sketchwars.physics.colliders.CharacterCollider;
import sketchwars.util.Converter;
import sketchwars.util.Timer;

/*
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class SketchCharacter implements GraphicsObject, GameObject {
    public static final int WAIT_AFTER_FIRE_TIME_MILLIS = 5000;
    public static final int DEFAULT_MAX_HEALTH = 100;
    private float posX;
    private float posY;
    private float width;
    private float height;
        
    private Texture texture;
    
    private AbstractWeapon weapon;
    private CharacterCollider coll;
    private int maxHealth;
    private int health;
    private boolean isDead;
    
    private Timer waitAfterFire;
    private AbstractProjectile firedProjectile;
    
    private boolean isFacingLeft;
    private float angle;

    private long vReticleOffset;
    private long vHealthBarOffset;
    private int lastActionTime; //last time input recieved
    
    private Texture reticleTexture;
    private HealthBar healthBar;
    
    private float lastMouseX;
    private boolean active;
    
    private AnimationSet<CharacterAnimations> animationSet;

    private boolean hasFired;

    
    public SketchCharacter() {
        this(DEFAULT_MAX_HEALTH, DEFAULT_MAX_HEALTH);
    }
    
    public SketchCharacter(int maxHealth, int health) {
        this.maxHealth = maxHealth;
        this.health = health;
        this.isDead = false;
        this.angle = 0.0f;
        this.isFacingLeft = false;//start facing right.
        reticleTexture = Texture.loadTexture("content/misc/reticle.png", false);
        vHealthBarOffset = Vectors.create(0, 0.1);
        
        waitAfterFire = new Timer(WAIT_AFTER_FIRE_TIME_MILLIS);
        hasFired = false;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setCollider(CharacterCollider coll) {
        this.coll = coll;
    }
    
    public void setWeapon(AbstractWeapon weapon) {
        this.weapon = weapon;
    }
    
    public void setHealthBar(HealthBar healthbar)
    {
        this.healthBar = healthbar;
        healthBar.setHealth(health);
        healthBar.setMaxHealth(maxHealth);
    }
    
    @Override
    public void update(double delta) {
        waitAfterFire.update(delta);
        updateCharacterInfo();
        coll.updateJumpTimer(delta);
        computeAngleFromMouse();        
        checkIfTimeToIdle();
        
        if (animationSet != null) {
            animationSet.setAnimationPosition(new Vector2d(posX, posY));
            animationSet.setAnimationDimension(new Vector2d(width, height));
            animationSet.update(delta);
        }
        
        if (weapon != null) {
            float fireAngle = getActualFireAngle();
            weapon.setAngle(fireAngle);
            weapon.setPosition(posX, posY);
            vReticleOffset = Vectors.createRTheta(0.1, fireAngle);
            weapon.update(delta);
        }
        
        if (healthBar != null)
        {
            healthBar.setPosition((float)posX + (float)Vectors.xComp(vHealthBarOffset),
                                  (float)posY + (float)Vectors.yComp(vHealthBarOffset));
            healthBar.setHealth(health);
            healthBar.update(delta);
        }
        
        if(health <= 0)
            isDead = true;
    }

    @Override
    public boolean hasExpired() {
        return isDead();
    }

    private void updateCharacterInfo() {
        BoundingBox bounds = coll.getBounds();
        long vCenter = bounds.getCenterVector();
        posX = Converter.PhysicsToGraphicsX(Vectors.xComp(vCenter));
        posY = Converter.PhysicsToGraphicsY(Vectors.yComp(vCenter));
        
        width = Converter.PhysicsToGraphicsX(bounds.getWidth());
        height = Converter.PhysicsToGraphicsY(bounds.getHeight());
    }
    
    @Override
    public void render() {
        if (animationSet != null) {
            animationSet.render();
        }
        
        if (weapon != null) {
            weapon.render();
            reticleTexture.draw(null, posX + (float)Vectors.xComp(vReticleOffset), posY + (float)Vectors.yComp(vReticleOffset), 0.05f, 0.05f);
        }
        
        if(healthBar != null)
        {
            healthBar.render();
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
    
    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosition(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
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
        waitAfterFire.reset();
    }

    public void notifyFired() {
        hasFired = true;
        waitAfterFire.restart();
    }

    public boolean hasFiredThisTurn() {
        return hasFired;
    }

    public boolean isTurnDone() {
        return hasFiredThisTurn() && waitAfterFire.hasElapsed();
    }

    public void fireCurrentWeapon(float power) {
        if(weapon != null) {
            firedProjectile = weapon.tryToFire(this, (float)power, Vectors.createRTheta(1.0f, getActualFireAngle()));
        }
    }

    public AbstractProjectile getFiredProjectile() {
        return firedProjectile;
    }

    public void aimUp(double elapsedMillis) {
        /*angle += Math.PI * elapsedMillis / 1000.0;
        //make sure not to aim higher than straight up
        angle = (float)Math.min(angle, Math.PI / 2.0);
        //System.out.println("angle: " + angle);*/
    }

    public void aimDown(double elapsedMillis) {
        /*angle -= Math.PI * elapsedMillis / 1000.0;
        //make sure not to aim lower than straight down
        angle = (float)Math.max(angle, -Math.PI / 2.0);
        //System.out.println("angle: " + angle);*/
    }

    public void moveLeft(double elapsedMillis) 
    {
        this.isFacingLeft = true;
        coll.moveLeft(elapsedMillis);
        
        lastActionTime = (int) System.currentTimeMillis();
        animationSet.setCurrentAnimation(CharacterAnimations.WALK_LEFT);
    }

    public void moveRight(double elapsedMillis)
    {
        this.isFacingLeft = false;
        coll.moveRight(elapsedMillis);
        
        lastActionTime = (int) System.currentTimeMillis();
        animationSet.setCurrentAnimation(CharacterAnimations.WALK_RIGHT);
    }
    
    public void stand()
    {
        coll.stand();
    }

    public void jump(double elapsedMillis)
    {
        if (coll.tryJump(elapsedMillis)) {
            
            lastActionTime = (int) System.currentTimeMillis();
            if (isFacingLeft) {
                animationSet.setCurrentAnimation(CharacterAnimations.JUMP_LEFT);
            } else {
                animationSet.setCurrentAnimation(CharacterAnimations.JUMP_RIGHT);
            }
        } 
    }

    private float getActualFireAngle() {
       /* if(isFacingLeft) {
            return (float)(Math.PI - angle);
        } else {
            
        }*/
        
        return angle;
    }

    public Collider getCollider() {
        return coll;
    }

    public AnimationSet<CharacterAnimations> getAnimationSet() {
        return animationSet;
    }

    public void setAnimationSet(AnimationSet<CharacterAnimations> animationSet) {
        this.animationSet = animationSet;
    }

    private void checkIfTimeToIdle() {
        int current = (int) System.currentTimeMillis();
        int diff = current - lastActionTime;
  
        if (diff > 200) {
            resetAnimationToIdle();
        }
    }
    
    public void resetAnimationToIdle() {
        if (isFacingLeft) {
            animationSet.setCurrentAnimation(CharacterAnimations.IDLE_LEFT);
        } else {
            animationSet.setCurrentAnimation(CharacterAnimations.IDLE_RIGHT);
        }
    }

    public void aimAt(float x, float y) {
        Vector2d direction = new Vector2d(x - posX, y - posY);
        direction.normalize();
        angle = (float) Math.atan2(direction.y, direction.x);

        if (x < posX) {
            isFacingLeft = true;
            animationSet.setCurrentAnimation(CharacterAnimations.WALK_LEFT);
        } else {
            isFacingLeft = false;
            animationSet.setCurrentAnimation(CharacterAnimations.WALK_RIGHT);
        }

    }

    private void computeAngleFromMouse() {
        if (active) {
            float mouseX = MouseHandler.xNormalized;
            float mouseY = MouseHandler.yNormalized;

            if (lastMouseX != mouseX) {
                aimAt(mouseX, mouseY);
            }
            lastMouseX = mouseX;
        }
    }
}

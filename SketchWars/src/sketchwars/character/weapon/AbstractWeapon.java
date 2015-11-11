package sketchwars.character.weapon;

import org.joml.Matrix3d;
import org.joml.Vector2d;
import sketchwars.Updateable;
import sketchwars.character.projectiles.*;
import sketchwars.graphics.*;
import sketchwars.character.SketchCharacter;
import sketchwars.physics.Vectors;
import sketchwars.util.*;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */

public abstract class AbstractWeapon implements Updateable, Drawable {
    public static final int INFINITE_AMMO = -1;
    
    private float rateOfFire; //per second
    private double lastTimeFired;
    private double elapsed;
            
    protected float posX;
    protected float posY;
    protected float width;
    protected float height;
    protected Texture texture;
    
    protected int ammo;
    protected ProjectileFactory projectileFactory;

    private float angle;
    /**
     * 
     * @param texture
     * @param width percentage of screen width
     * @param height percentage of screen height
     * @param projectileFactory 
     */
    public AbstractWeapon(Texture texture, float width, float height, ProjectileFactory projectileFactory) {
        this.texture = texture;
        this.projectileFactory = projectileFactory;
        
        rateOfFire = 1;
        ammo = INFINITE_AMMO;
        elapsed = Integer.MAX_VALUE;
        this.width = width;
        this.height = height;
        angle = 0;
    }
    
    @Override
    public void render() {
        if (texture != null) {
            long weaponOffset = Vectors.createRTheta(0.03, angle);
            double offsetX = Vectors.xComp(weaponOffset);
            double offsetY = Vectors.yComp(weaponOffset);
            
            Matrix3d transform = new Matrix3d();
            Matrix3d rotate = new Matrix3d();
            Matrix3d scale = new Matrix3d();
            
            rotate.rotate(angle, 0, 0, 1);
            transform.translation(new Vector2d(posX + offsetX, posY + offsetY));
            
            if (angle < -Math.PI/2.0f || angle > Math.PI/2.0f) {
                scale.scale(width, -height, 1);
            } else {
                scale.scale(width, height, 1);
            }
         
            transform.mul(rotate);
            transform.mul(scale);
            
            texture.draw(transform);
        }
    }
    
    @Override
    public void update(double elapsed) {
        this.elapsed += elapsed;
    }

    public void aimAt(float x, float y) {
        Vector2d direction = new Vector2d(x - posX, y - posY);
        direction.normalize();
        angle = (float) Math.atan2(direction.y, direction.x);
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

    public double getAngle() {
        return angle;
    }
    
    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
    
    public void setPosition(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public double getWidth() {
        return width;
    }

    /**
     * 
     * @param width percentage of screen width
     * @param height percentage of screen height
     */
    public void setDimension(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public double getHeight() {
        return height;
    }
   
    
    public AbstractProjectile tryToFire(SketchCharacter owner, float power) {
        owner.notifyFired();
        return fire(owner, power, Vectors.createRTheta(1.0f, angle));
    }
    
    public AbstractProjectile tryToFire(SketchCharacter owner, float power, float xNormalized, float yNormalized) {
        aimAt(xNormalized, yNormalized);
        return tryToFire(owner, power);
    }

    public void resetFire() {
        lastTimeFired = 0;
    }

    private AbstractProjectile fire(SketchCharacter owner, float power, long vAimDirection) {
        long normalDir = Vectors.normalize(vAimDirection);
        long vVelocity = Vectors.scalarMultiply(getProjectileSpeed(power), normalDir);
        long vPosition = Vectors.add(owner.getCollider().getPosition(), Vectors.scaleToLength(normalDir, 100.0));
        return createProjectile(owner, vPosition, vVelocity);
    }

    protected abstract AbstractProjectile createProjectile(SketchCharacter owner, long vPosition, long vVelocity);

    protected abstract double getProjectileSpeed(float power);

    public float getRateOfFire() {
        return rateOfFire;
    }

    /**
     * per second
     * @param rateOfFire 
     */
    public void setRateOfFire(float rateOfFire) {
        this.rateOfFire = rateOfFire;
    }

    public int getAmmo()
    {
        return ammo;
    }
    public void setAmmo(int ammo)
    {
        this.ammo = ammo;
    }
    public void increaseAmmo(int num)
    {
        ammo += num;
    }
    public void decreaseAmmo(int num)
    {
        if(ammo != INFINITE_AMMO)
        {
            ammo -= num;
            ammo = Math.max(ammo, 0);
        }
    }
}

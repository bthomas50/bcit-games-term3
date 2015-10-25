package sketchwars.character.weapon;

import org.joml.Matrix3d;
import org.joml.Vector2d;
import sketchwars.Updateable;
import sketchwars.character.projectiles.*;
import sketchwars.graphics.Drawable;
import sketchwars.graphics.Texture;
import sketchwars.character.SketchCharacter;
import sketchwars.physics.Vectors;

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
    }
    
    @Override
    public void render() {
        if (texture != null) {
            float xOffset = width/2;
            long vReticleOffset = Vectors.createRTheta(0.03, angle);
            
            Matrix3d matrix = new Matrix3d();
            
            matrix.translation(new Vector2d(posX + (float)Vectors.xComp(vReticleOffset), 
                    posY + (float)Vectors.yComp(vReticleOffset)));
            
            matrix.rotateZ(angle);
            
            if (angle >= Math.PI/2.0f) {
                matrix.scale(width, -height, 1);
            } else {
                matrix.scale(width, height, 1);
            }
            
            texture.draw(matrix);
        }
    }
    
    @Override
    public void update(double elapsed) {
        this.elapsed += elapsed;
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
   
    
    public boolean tryToFire(SketchCharacter owner, float power, long vAimDirection) {
        double timeFired = elapsed;
        double timeSinceLastFired = timeFired - lastTimeFired;
        float rateOfFireInMilli = 1000/rateOfFire;
                            
        if (timeSinceLastFired > rateOfFireInMilli) {
            fire(owner, power, vAimDirection);
            lastTimeFired = timeFired;
            return true;
        } else {
            return false;
        }
    }

    private void fire(SketchCharacter owner, float power, long vAimDirection) {
        long normalDir = Vectors.normalize(vAimDirection);
        long vVelocity = Vectors.scalarMultiply(getProjectileSpeed(power), normalDir);
        long vPosition = Vectors.add(owner.getCollider().getPosition(), Vectors.scaleToLength(normalDir, 100.0));
        AbstractProjectile projectile = createProjectile(owner, vPosition, vVelocity);
    }

    protected abstract AbstractProjectile createProjectile(SketchCharacter owner, long vPosition, long vVelocity);

    protected abstract double getProjectileSpeed(float power);

    protected long getFireDirection(long vAimDirection) {
        return vAimDirection;
    }

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

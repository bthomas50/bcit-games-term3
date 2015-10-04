package sketchwars.character.projectiles;

import sketchwars.graphics.GraphicsObject;
import sketchwars.graphics.Texture;
import sketchwars.physics.*;
import sketchwars.GameObject;
/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public abstract class AbstractProjectile implements GraphicsObject, GameObject{
    protected Collider coll;
    protected Texture texture;
    protected float power;
    protected long direction;
    
    protected float mass;
    protected float elasticity;
    protected long velocity;
    
    public AbstractProjectile(Texture texture) {
        this.texture = texture;
    }
     
    @Override
    public void update(double elapsedMillis) {

    }

    public void setCollider(Collider coll) {
        this.coll = coll;
    }

    @Override
    public void render() {
        if (texture != null) {
            BoundingBox bounds = coll.getBounds();
            long vCenter = bounds.getCenterVector();
            texture.drawNormalized(Vectors.xComp(vCenter) / 1024.0 , Vectors.yComp(vCenter) / 1024.0, (double) bounds.getWidth() / 2048.0, (double) bounds.getHeight() / 2048.0);
        }
    }
    
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }

    public Collider getCollider() {
        return coll;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public void setDirection(long direction) {
        this.direction = direction;
    }

    public float getMass() {
        return mass;
    }

    public float getElasticity() {
        return elasticity;
    }

    public long getVelocity() {
        return velocity;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public void setElasticity(float elasticity) {
        this.elasticity = elasticity;
    }

    public void setVelocity(long velocity) {
        this.velocity = velocity;
    }
}

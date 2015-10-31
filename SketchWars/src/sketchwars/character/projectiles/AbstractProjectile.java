package sketchwars.character.projectiles;

import org.joml.Matrix3d;
import sketchwars.graphics.GraphicsObject;
import sketchwars.graphics.Texture;
import sketchwars.physics.*;
import sketchwars.game.GameObject;
import sketchwars.character.SketchCharacter;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public abstract class AbstractProjectile implements GraphicsObject, GameObject{
    protected Collider coll;
    protected Texture texture;
    
    protected int damage;
    protected boolean expired;
    //the character that fired this projectile (we can ignore collisions with it if we want)
    protected SketchCharacter owner; 
    
    public AbstractProjectile(Texture texture, SketchCharacter owner, int damage) {
        this.texture = texture;
        this.owner = owner;
        this.damage = damage;
        this.expired = false;
    }
     
    @Override
    public void update(double elapsedMillis) { }

    @Override
    public boolean hasExpired() {
        return expired;
    }

    public Collider getCollider() {
        return coll;
    }
    
    public void setCollider(Collider coll) {
        this.coll = coll;
        coll.addCollisionListener(new ProjectileCollisionHandler());
    }

    @Override
    public void render() {
        if (texture != null) {
            BoundingBox bounds = coll.getBounds();
            long vCenter = bounds.getCenterVector();
            Matrix3d transform = new Matrix3d();
            Matrix3d rotate = new Matrix3d();
            Matrix3d scale = new Matrix3d();
            
            transform.translation(Vectors.xComp(vCenter) / 1024.0f, Vectors.yComp(vCenter) / 1024.0f);
            
            long velocity = coll.getVelocity();
            float angle = (float)Math.atan2(Vectors.yComp(velocity), Vectors.xComp(velocity));
            
            rotate.rotate(angle, 0, 0, 1);
            
            if (angle >= Math.PI/2.0f) {
                scale.scale(-bounds.getWidth() / 1024.0f, -bounds.getHeight() / 1024.0f, 1);
            } else {
                scale.scale(bounds.getWidth() / 1024.0f, bounds.getHeight() / 1024.0f, 1);
            }
            
            transform.mul(rotate);
            transform.mul(scale);
            
            texture.draw(transform);
        }
    }
    
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }

    protected abstract void handleCollisionWithCharacter(SketchCharacter ch);

    protected abstract void handleCollision(Collider other);

    private class ProjectileCollisionHandler implements CollisionListener {
        @Override
        public void collided(Collider thisColl, Collider otherColl) {
            handleCollision(otherColl);
            if(otherColl.hasAttachedGameObject()) {
                GameObject otherObj = otherColl.getAttachedGameObject();
                if(otherObj instanceof SketchCharacter) {
                    handleCollisionWithCharacter((SketchCharacter)otherObj);
                }
            }
        }
    }
}

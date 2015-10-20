package sketchwars.character.projectiles;

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
            texture.drawNormalized(Vectors.xComp(vCenter) / 1024.0 , Vectors.yComp(vCenter) / 1024.0, (double) bounds.getWidth() / 1024.0, (double) bounds.getHeight() / 1024.0);
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

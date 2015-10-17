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
public abstract class BasicProjectile implements GraphicsObject, GameObject{
    protected Collider coll;
    protected Texture texture;
    
    protected int damage;
    private boolean expired;
    //to prevent taking damage to it self (unless grenade)
    private SketchCharacter owner; 
    
    public BasicProjectile(Texture texture, SketchCharacter owner, int damage) {
        this.texture = texture;
        this.owner = owner;
        this.damage = damage;
        this.expired = false;
    }
     
    @Override
    public void update(double elapsedMillis) {
    }

    @Override
    public boolean hasExpired() {
        return expired;
    }

    public Collider getCollider() {
        return coll;
    }
    
    public void setCollider(Collider coll) {
        this.coll = coll;
        coll.addCollisionListener(new BasicCollisionHandler());
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

    protected void handleCollisionWithCharacter(SketchCharacter ch) {
        if(ch != owner) {
            ch.takeDamage(damage);
            System.out.println(ch + " is hit for " + damage + " damage.");
            expired = true;
        }
    }

    private class BasicCollisionHandler implements CollisionListener {
        @Override
        public void collided(Collider thisColl, Collider otherColl) {
            if(otherColl.hasAttachedGameObject()) {
                GameObject otherObj = otherColl.getAttachedGameObject();
                if(otherObj instanceof SketchCharacter) {
                    handleCollisionWithCharacter((SketchCharacter)otherObj);
                }
            }
        }
    }
}

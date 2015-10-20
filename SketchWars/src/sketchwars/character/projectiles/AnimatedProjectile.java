package sketchwars.character.projectiles;

import sketchwars.animation.*;
import sketchwars.game.*;
import sketchwars.character.SketchCharacter;
import sketchwars.graphics.*;
import sketchwars.physics.*;

public class AnimatedProjectile implements GameObject, GraphicsObject
{
    private Animation animation;
    protected Collider coll;
    private int damage;

    public AnimatedProjectile(Animation anim, int damage)
    {
        animation = anim;
        anim.start();
        this.damage = damage;
    }

    public void setCollider(Collider coll) {
        this.coll = coll;
        coll.addCollisionListener(new ProjectileCollisionHandler());
    }

    @Override
    public void update(double elapsedMillis) 
    {
        animation.update(elapsedMillis);
    }

    @Override
    public boolean hasExpired()
    {
        return animation.hasExpired();
    }

    @Override
    public void render()
    {
        animation.render();
    }

    private void handleCollisionWithCharacter(SketchCharacter other) {
        other.takeDamage(damage);
        System.out.println(other + " is hit for " + damage + " damage.");
    }

    private class ProjectileCollisionHandler implements CollisionListener {
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
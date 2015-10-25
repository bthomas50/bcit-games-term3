package sketchwars.character.projectiles;

import java.util.HashSet;
import sketchwars.animation.*;
import sketchwars.game.*;
import sketchwars.character.SketchCharacter;
import sketchwars.graphics.*;
import sketchwars.physics.*;

public class AnimatedProjectile implements GameObject, GraphicsObject
{
    private HashSet<SketchCharacter> damagedCharacters; 
    private HashSet<Collider> pushedObjects;
    private Animation animation;
    protected Collider coll;
    private int damage;

    public AnimatedProjectile(Animation anim, int damage)
    {
        animation = anim;
        pushedObjects = new HashSet<>();
        anim.start();
        this.damage = damage;
        
        damagedCharacters = new HashSet<>();
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
        if (!damagedCharacters.contains(other)) {
            other.takeDamage(damage);
            damagedCharacters.add(other);
            System.out.println(other + " is hit for " + damage + " damage.");
        }
    }

    protected void handleCollision(Collider c) {
        if(!pushedObjects.contains(c))
        {
            long vCenter = coll.getBounds().getCenterVector();
            System.out.println("Explosion Center: " + Vectors.toString(vCenter));
            long vExplosionForceCenter = Vectors.add(vCenter, Vectors.create(0, -coll.getBounds().getHeight() / 3.0f));
            System.out.println("Force Center: " + Vectors.toString(vExplosionForceCenter));
            long vOtherCenter = c.getBounds().getCenterVector();
            System.out.println("Other Center: " + Vectors.toString(vOtherCenter));
            long vDelta = Vectors.subtract(vOtherCenter, vExplosionForceCenter);
            System.out.println("DeltaForce: " + Vectors.toString(vDelta));
            long vDeltaCenter = Vectors.subtract(vOtherCenter, vCenter);
            System.out.println("DeltaPosition: " + Vectors.toString(vDeltaCenter));
            double power = 8000.0 - 20.0 * Vectors.length(vDeltaCenter);
            if(power < 0) power = 0.0;
            c.applyForce(Vectors.scaleToLength(vDelta, power), 1000.0);
            pushedObjects.add(c);
        }
    }

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
package sketchwars.physics;

import sketchwars.game.GameObject;

public interface Collider extends PhysicsObject
{
    BoundingBox getBounds();
    long getCenterOfMass();
    float getElasticity();
    void setElasticity(float ela);
    boolean isStatic();

    void notify(Collider other);
    void addCollisionListener(CollisionListener list);
    void removeCollisionListener(CollisionListener list);

    boolean hasAttachedGameObject();
    GameObject getAttachedGameObject();
}
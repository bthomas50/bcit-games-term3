package sketchwars.physics;

public interface Collider extends PhysicsObject
{
    BoundingBox getBounds();

    void notify(Collider other);
    void addCollisionListener(CollisionListener list);
    void removeCollisionListener(CollisionListener list);
}
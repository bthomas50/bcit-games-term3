package sketchwars.physics;

public interface CollisionListener
{
    void collided(Collider thisColl, Collider otherColl);
}
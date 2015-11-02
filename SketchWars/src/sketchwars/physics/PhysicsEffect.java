package sketchwars.physics;

//defines effects (i.e. a force or acceleration) that can be applied to physics objects by the Physics class.
public interface PhysicsEffect
{
    void apply(PhysicsObject obj, double elapsedMillis);
}
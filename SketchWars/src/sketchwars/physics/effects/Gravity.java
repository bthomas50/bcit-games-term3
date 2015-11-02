package sketchwars.physics.effects;

import sketchwars.physics.*;

public class Gravity implements PhysicsEffect
{
    private static final long V_GRAVITY = Vectors.create(0, -600);

    @Override
    public void apply(PhysicsObject obj, double elapsedMillis)
    {
        if(!obj.isStatic())
        {
            obj.accelerate(V_GRAVITY, elapsedMillis);
        }
    }
}
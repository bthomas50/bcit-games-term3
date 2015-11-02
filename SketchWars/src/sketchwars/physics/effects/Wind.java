package sketchwars.physics.effects;

import sketchwars.physics.*;

public class Wind implements PhysicsEffect
{
    private static final double MAX_PRESSURE = 1.5;
    private static final double MAX_DELTA = 300;
    private long vVelocity;

    public Wind(long vVelocity)
    {
        this.vVelocity = vVelocity;
    }
    
    @Override
    public void apply(PhysicsObject obj, double elapsedMillis)
    {
        obj.applyForce(calculateForce(obj), elapsedMillis);
    }

    private long calculateForce(PhysicsObject obj)
    {
        if(!(obj instanceof Collider))
        {
            return Vectors.V_ZERO;
        }
        Collider coll = (Collider) obj;
        double perpArea = coll.getBounds().getHeight();
        long vRelativeVelocity = Vectors.subtract(vVelocity, coll.getVelocity());
        long vVelocityNorm = Vectors.normalize(vVelocity);
        double aligned = Vectors.dot(vRelativeVelocity, vVelocityNorm);
        double magnitude = perpArea * (Math.min(aligned, MAX_DELTA) / MAX_DELTA) * MAX_PRESSURE;
        return Vectors.scalarMultiply(vVelocityNorm, magnitude);
    }
}
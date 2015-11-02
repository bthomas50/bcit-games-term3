package sketchwars.physics;

public class BasicPhysicsObject implements PhysicsObject
{
    protected long vPosition;
    protected long vVelocity;
	protected float mass;
    public BasicPhysicsObject()
    {
        this(Vectors.create(0,0), Vectors.create(0,0), 0);
    }
    public BasicPhysicsObject(long vPosition)
    {
        this(vPosition, Vectors.create(0,0), 0);
    }
    public BasicPhysicsObject(long vPosition, long vVelocity)
    {
		this(vPosition, vVelocity, 0);
    }
	public BasicPhysicsObject(long vPosition, long vVelocity, float mass)
	{
        this.vPosition = vPosition;
        this.vVelocity = vVelocity;
		this.mass = mass;
	}
    @Override
    public boolean isStatic()
    {
        return mass <= 0.0f;
    }
    @Override
    public long getPosition()
    {
        return vPosition;
    }
    @Override
    public void setPosition(long vPos)
    {
        vPosition = vPos;
    }
    @Override
    public long getVelocity()
    {
        return vVelocity;
    }
    @Override
    public void setVelocity(long vVel)
    {
        vVelocity = vVel;
    }
    @Override
	public void setMass(float m)
	{
		mass = m;
	}
    @Override
	public float getMass()
	{
		return mass;
	}
    @Override
	public void accelerate(long vAccel, double elapsedMillis)
	{
		vVelocity = Vectors.add(vVelocity, Vectors.scalarMultiply(vAccel, elapsedMillis/1000));
	}
    @Override
	public void applyForce(long vForce, double elapsedMillis)
	{
		if(mass > 0.0f)
		{
			vVelocity = Vectors.add(vVelocity, Vectors.scalarMultiply(vForce, elapsedMillis/(mass * 1000)));
		}
	}
    @Override
    public boolean hasExpired()
    {
        return false;
    }
}
package sketchwars.physics;

public class BasicPhysicsObject implements PhysicsObject
{
    protected long vPosition;
    protected long vVelocity;

    public BasicPhysicsObject()
    {
        this(Vectors.create(0,0), Vectors.create(0,0));
    }
    public BasicPhysicsObject(long vPosition)
    {
        this(vPosition, Vectors.create(0,0));
    }
    public BasicPhysicsObject(long vPosition, long vVelocity)
    {
        this.vPosition = vPosition;
        this.vVelocity = vVelocity;
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
}
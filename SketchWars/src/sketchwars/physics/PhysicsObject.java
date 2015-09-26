package sketchwars.physics;

public interface PhysicsObject
{
    void setPosition(long vPos);
    long getPosition();
    void setVelocity(long vVel);
    long getVelocity();
	void setMass(float mass);
	float getMass();
	void accelerate(long vAccel, double elapsedMillis);
	void applyForce(long vForce, double elapsedMillis);
}
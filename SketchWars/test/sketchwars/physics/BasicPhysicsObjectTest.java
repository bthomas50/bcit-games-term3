package sketchwars.physics;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class BasicPhysicsObjectTest
{
	private static final long ZERO;
	static
	{
		ZERO = Vectors.create(0,0);
	}
	
	@Test
	public void testCreateDefault()
	{
		BasicPhysicsObject obj = new BasicPhysicsObject();
		assertEquals(ZERO, obj.getPosition());
		assertEquals(ZERO, obj.getVelocity());
		assertEquals(0.0f, obj.getMass(), 0.0f);
	}
	
	@Test
	public void testCreatePos()
	{
		BasicPhysicsObject obj = new BasicPhysicsObject(Vectors.create(12, 12));
		assertEquals(Vectors.create(12, 12), obj.getPosition());
		assertEquals(ZERO, obj.getVelocity());
		assertEquals(0.0f, obj.getMass(), 0.0f);
	}
	
	@Test
	public void testCreatePosVel()
	{
		BasicPhysicsObject obj = new BasicPhysicsObject(Vectors.create(12, 12), Vectors.create(5,-15));
		assertEquals(Vectors.create(12, 12), obj.getPosition());
		assertEquals(Vectors.create(5,-15), obj.getVelocity());
		assertEquals(0.0f, obj.getMass(), 0.0f);
	}
	
	@Test
	public void testCreatePosVelMass()
	{
		BasicPhysicsObject obj = new BasicPhysicsObject(Vectors.create(12, 12), Vectors.create(5,-15), 2.5f);
		assertEquals(Vectors.create(12, 12), obj.getPosition());
		assertEquals(Vectors.create(5,-15), obj.getVelocity());
		assertEquals(2.5f, obj.getMass(), 0.0f);
	}
	
	@Test
	public void testAccelerate()
	{
		BasicPhysicsObject obj = new BasicPhysicsObject(ZERO, ZERO, 0.0f);
		obj.accelerate(Vectors.create(0, 1), 1000);
		assertEquals(0.0f, Vectors.xComp(obj.getVelocity()), Vectors.EPSILON);
		assertEquals(1.0f, Vectors.yComp(obj.getVelocity()), Vectors.EPSILON);
	}
	
	@Test
	public void testApplyForce()
	{
		BasicPhysicsObject obj = new BasicPhysicsObject(ZERO, Vectors.create(2, 1), 3.0f);
		obj.applyForce(Vectors.create(0, 1), 1000);
		assertEquals(2.0f, Vectors.xComp(obj.getVelocity()), Vectors.EPSILON);
		assertEquals(4.0f / 3.0f, Vectors.yComp(obj.getVelocity()), Vectors.EPSILON);
	}
	
	@Test
	public void testApplyForceZeroMassDoesNothing()
	{
		BasicPhysicsObject obj = new BasicPhysicsObject(ZERO, Vectors.create(2, 1), 0.0f);
		obj.applyForce(Vectors.create(0, 1), 1000);
		assertEquals(2.0f, Vectors.xComp(obj.getVelocity()), Vectors.EPSILON);
		assertEquals(1.0f, Vectors.yComp(obj.getVelocity()), Vectors.EPSILON);
	}
}
package sketchwars.physics;

import java.util.List;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class PhysicsTest
{
    private static final long[][] rectangularData;
    private static final long[][] irregularData;
    private static final long[][] dataWithExtraZeros;
    static
    {
        rectangularData = new long[2][2];
        rectangularData[0][0] = 0xFFFFFFFFFFFFFFFFl;
        rectangularData[1][0] = 0xFFFFFFFFFFFFFFFFl;
        rectangularData[0][1] = 0xFFFFFFFFFFFFFFFFl;
        rectangularData[1][1] = 0xFFFFFFFFFFFFFFFFl;

        irregularData = new long[2][];
        irregularData[0] = new long[1];
        irregularData[1] = null;
        irregularData[0][0] = 0xFFFFFFFFFFFFFFFFl;

        dataWithExtraZeros = new long[2][1];
        dataWithExtraZeros[0][0] = 0x0000000000000000l;
        dataWithExtraZeros[1][0] = 0x00000FFFFFF00000l;
    }

    private BitMask rectMask;
    private BitMask irregMask;
    private BitMask wideMask;
    private PixelCollider rectCollider;
    private PixelCollider irregCollider;
    private PixelCollider wideCollider;
    private TestListener list;
	private Physics physics;
	
	@Before
	public void setup()
	{
        list = new TestListener();
        rectMask = new BitMask(rectangularData);
        irregMask = new BitMask(irregularData);
        wideMask = new BitMask(dataWithExtraZeros);
        rectCollider = new PixelCollider(rectMask);
        irregCollider = new PixelCollider(irregMask);
        wideCollider = new PixelCollider(wideMask);
        rectCollider.addCollisionListener(list);
        irregCollider.addCollisionListener(list);
        wideCollider.addCollisionListener(list);
		physics = new Physics(new BoundingBox(-256, -256, 256, 256));
	}
	
	@Test
	public void testAddCollider()
	{
		physics.addCollider(rectCollider);
		physics.addCollider(wideCollider);
		List<Collider> colliders = physics.getColliders();
		assertTrue(colliders.contains(rectCollider));
		assertTrue(colliders.contains(wideCollider));
		List<PhysicsObject> physicsObjects = physics.getPhysicsObjects();
		assertTrue(physicsObjects.contains(rectCollider));
		assertTrue(physicsObjects.contains(wideCollider));
	}
	
	@Test
	public void testAddPhysicsObject()
	{
		physics.addPhysicsObject(rectCollider);
		List<Collider> colliders = physics.getColliders();
		List<PhysicsObject> physicsObjects = physics.getPhysicsObjects();
		assertTrue(physicsObjects.contains(rectCollider));
		assertFalse(colliders.contains(rectCollider));
	}
	
	@Test
	public void testUpdateDoesCollisionDetection()
	{
        rectCollider.setMass(1);
        wideCollider.setMass(1);
		physics.addCollider(rectCollider);
		physics.addCollider(wideCollider);
		physics.update(0);//time not considered yet.
		assertEquals(2, list.collisions.size());
	}
	
	@Test
	public void testCollisionDetectionOnlyConsidersColliders()
	{
		physics.addCollider(rectCollider);
		physics.addPhysicsObject(wideCollider);
		physics.update(0);
		assertEquals(0, list.collisions.size());
	}
	
	@Test
	public void testOOBObjectsAreNotConsideredForCollisionDetection()
	{
		rectCollider.setPosition(Vectors.create(1024, 1024));
		wideCollider.setPosition(Vectors.create(1024, 1024));
		physics.addCollider(rectCollider);
		physics.addCollider(wideCollider);
		physics.update(0);
		assertEquals(0, list.collisions.size());
	}
	
	@Test
	public void testUpdateAppliesGravityToNonStaticObjects()
	{
        rectCollider.setMass(1.0f);
		physics.addPhysicsObject(rectCollider);
		physics.update(1000);
		assertEquals(0.0, Vectors.xComp(rectCollider.getVelocity()), Vectors.EPSILON);
		assertEquals(-98.0, Vectors.yComp(rectCollider.getVelocity()), Vectors.EPSILON);
	}
	
	@Test
	public void testUpdateMovesThings()
	{
		rectCollider.setVelocity(Vectors.create(3, 0));
		physics.addPhysicsObject(rectCollider);
		physics.update(1000);
		//because of initial velocity
		assertEquals(3.0, Vectors.xComp(rectCollider.getPosition()), Vectors.EPSILON);
		assertEquals(3.0, Vectors.xComp(rectCollider.getVelocity()), Vectors.EPSILON);
	}
	@Test
	public void testUpdateClipsVelocity()
	{
		rectCollider.setVelocity(Vectors.create(-1024, -1024));
		physics.addPhysicsObject(rectCollider);
		physics.update(0);//so no acceleration due to gravity
		long vNewVelocity = rectCollider.getVelocity();
		assertEquals(1000.0, Vectors.length(vNewVelocity), Math.sqrt(Vectors.EPSILON));
		assertEquals(Vectors.direction(Vectors.create(-1, -1)), Vectors.direction(vNewVelocity), Vectors.EPSILON);
	}
}
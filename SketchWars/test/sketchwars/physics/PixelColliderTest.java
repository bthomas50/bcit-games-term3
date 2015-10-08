package sketchwars.physics;

import java.util.ArrayList;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class PixelColliderTest
{
    PixelCollider coll1;
    PixelCollider coll2;
    TestListener list;

    @Before
    public void setup()
    {
        coll1 = new PixelCollider(new BitMask());
        coll2 = new PixelCollider(new BitMask());
        list = new TestListener();
    }

    @Test
    public void testCreate()
    {
        assertEquals(BoundingBox.EMPTY, coll1.getBounds());
        assertEquals(Vectors.create(0,0), coll1.getPosition());
        assertEquals(Vectors.create(0,0), coll1.getVelocity());
    }

    @Test
    public void testNotifyListener()
    {
        coll1.addCollisionListener(list);
        coll1.notify(coll2);
        assertEquals(1, list.collisions.size());
    }

    @Test
    public void testAddListenerTwice()
    {
        coll1.addCollisionListener(list);
        coll1.addCollisionListener(list);
        coll1.notify(coll2);
        assertEquals(2, list.collisions.size());
    }

    @Test
    public void testRemoveListener()
    {
        coll1.addCollisionListener(list);
        coll1.removeCollisionListener(list);
        coll1.notify(coll2);
        assertEquals(0, list.collisions.size());
    }

    @Test
    public void testAddListenerTwiceAndRemove()
    {
        coll1.addCollisionListener(list);
        coll1.addCollisionListener(list);
        coll1.removeCollisionListener(list);
        coll1.notify(coll2);
        assertEquals(1, list.collisions.size());
    }
	
	@Test
	public void testSetPositionUpdatesBitMask()
	{
		long vPosition = Vectors.create(10, -10);
		coll1.setPosition(vPosition);
		assertEquals(vPosition, coll1.getPixels().getPosition());
	}
}
package sketchwars.physics;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import java.util.List;

public class QuadTreeTest
{
    private class TestCollider extends BasicPhysicsObject implements Collider
    {
        private BoundingBox bounds;

        public TestCollider(BoundingBox bounds)
        {
            this.bounds = bounds;
        }
        public TestCollider(int top, int left, int bottom, int right)
        {
            bounds = new BoundingBox(top, left, bottom, right);
        }
        @Override
        public BoundingBox getBounds()
        {
            return bounds;
        }

        @Override
        public void notify(Collider other)
        {
            throw new UnsupportedOperationException("this is a test object!");
        }
        @Override
        public void addCollisionListener(CollisionListener list)
        {
            throw new UnsupportedOperationException("this is a test object!");
        }
        @Override
        public void removeCollisionListener(CollisionListener list)
        {
            throw new UnsupportedOperationException("this is a test object!");
        }
    }
    QuadTree qt;
    Collider boxInBounds;
    Collider boxOutOfBounds;

    @Before
    public void initQuadTree()
    {
        qt = new QuadTree(new BoundingBox(0, 0, 10, 10), 5, 2);
        boxInBounds = new TestCollider(0,0,5,5);
        boxOutOfBounds = new TestCollider(0,0,15,15);
    }

    @Test
    public void testCreate()
    {
        assertEquals(qt.count(), 0);
    }
    @Test
    public void testInsertInBounds()
    {
        boolean result = qt.insert(boxInBounds);
        assertTrue(qt.contains(boxInBounds));
        assertTrue(result);
    }
    @Test
    public void testInsertOutOfBounds()
    {
        boolean result = qt.insert(boxOutOfBounds);
        assertFalse(qt.contains(boxOutOfBounds));
        assertFalse(result);
    }
    @Test
    public void testRemove()
    {
        qt.insert(boxInBounds);
        qt.remove(boxInBounds);
        assertFalse(qt.contains(boxInBounds));
        assertEquals(qt.count(), 0);
    }
    @Test
    public void testRemoveNotThere()
    {
        qt.insert(boxInBounds);
        qt.remove(boxOutOfBounds);
        assertTrue(qt.contains(boxInBounds));
        assertEquals(qt.count(), 1);
    }
    @Test
    public void testReplace()
    {
        qt.insert(boxInBounds);
        TestCollider otherBoxInBounds = new TestCollider(2,2,4,4);
        qt.replace(boxInBounds, otherBoxInBounds);
        assertTrue(qt.contains(otherBoxInBounds));
        assertFalse(qt.contains(boxInBounds));
    }
    @Test
    public void testReplaceWithOOB()
    {
        qt.insert(boxInBounds);
        qt.replace(boxInBounds, boxOutOfBounds);
        assertFalse(qt.contains(boxOutOfBounds));
        assertFalse(qt.contains(boxInBounds));
    }
    @Test
    public void testCount()
    {
        for(int i = 0; i < 15; i++)
        {
            qt.insert(new TestCollider(0,0,1 + (i/5),1 + (i/5)));
        }
        assertEquals(15, qt.count());
    }
    @Test
    public void testClear()
    {
        for(int i = 0; i < 15; i++)
        {
            qt.insert(new TestCollider(0,0,1 + (i/5),1 + (i/5)));
        }
        qt.clear();
        assertEquals(qt.count(), 0);
    }
    @Test
    public void testPrune()
    {
        TestCollider[] allColliders = new TestCollider[15];
        //insert a bunch of Colliders
        for(int i = 0; i < 15; i++)
        {
            allColliders[i] = new TestCollider(0,0,1 + (i/5),1 + (i/5));
            qt.insert(allColliders[i]);
        }
        //remove some of them
        for(int i = 0; i < 10; i++)
        {
            qt.remove(allColliders[i]);
        }
        qt.prune();
        assertEquals(qt.count(), 5);
    }
    @Test
    public void testRetrieve()
    {
        TestCollider[] queryColliders = new TestCollider[10];
        for(int i = 0; i < 10; i++)
        {
            queryColliders[i] = new TestCollider(0,0,1 + (i/5), 1 + (i/5));
            qt.insert(queryColliders[i]);
        }
        //insert some extra Colliders that won't fall in the same subtree.
        TestCollider[] otherColliders = new TestCollider[10];
        for(int i = 0; i < 10; i++)
        {
            otherColliders[i] = new TestCollider(8,8,8+(i/5),8+(i/5));
            qt.insert(otherColliders[i]);
        }
        List<Collider> results = qt.retrieve(new BoundingBox(0,0,2,2));
        for(int i = 0; i < 10; i++)
        {
            assertTrue(results.contains(queryColliders[i]));
            assertFalse(results.contains(otherColliders[i]));
        }
    }
}
package Physics;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import java.util.List;

public class QuadTreeTest
{
    QuadTree qt;
    BoundingBox boxInBounds;
    BoundingBox boxOutOfBounds;

    @Before
    public void initQuadTree()
    {
        qt = new QuadTree(new BoundingBox(0, 0, 10, 10), 5, 2);
        boxInBounds = new BoundingBox(0,0,5,5);
        boxOutOfBounds = new BoundingBox(0,0,15,15);
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
        BoundingBox otherBoxInBounds = new BoundingBox(2,2,4,4);
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
            qt.insert(new BoundingBox(0,0,1 + (i/5),1 + (i/5)));
        }
        assertEquals(15, qt.count());
    }
    @Test
    public void testClear()
    {
        for(int i = 0; i < 15; i++)
        {
            qt.insert(new BoundingBox(0,0,1 + (i/5),1 + (i/5)));
        }
        qt.clear();
        assertEquals(qt.count(), 0);
    }
    @Test
    public void testPrune()
    {
        BoundingBox[] allBoxes = new BoundingBox[15];
        //insert a bunch of boxes
        for(int i = 0; i < 15; i++)
        {
            allBoxes[i] = new BoundingBox(0,0,1 + (i/5),1 + (i/5));
            qt.insert(allBoxes[i]);
        }
        //remove some of them
        for(int i = 0; i < 10; i++)
        {
            qt.remove(allBoxes[i]);
        }
        qt.prune();
        assertEquals(qt.count(), 5);
    }
    @Test
    public void testRetrieve()
    {
        BoundingBox[] queryBoxes = new BoundingBox[10];
        for(int i = 0; i < 10; i++)
        {
            queryBoxes[i] = new BoundingBox(0,0,1 + (i/5), 1 + (i/5));
            qt.insert(queryBoxes[i]);
        }
        //insert some extra boxes that won't fall in the same subtree.
        BoundingBox[] otherBoxes = new BoundingBox[10];
        for(int i = 0; i < 10; i++)
        {
            otherBoxes[i] = new BoundingBox(8,8,8+(i/5),8+(i/5));
            qt.insert(otherBoxes[i]);
        }
        List<BoundingBox> results = qt.retrieve(new BoundingBox(0,0,2,2));
        for(int i = 0; i < 10; i++)
        {
            assertTrue(results.contains(queryBoxes[i]));
            assertFalse(results.contains(otherBoxes[i]));
        }
    }
}
package Physics;

import static org.junit.Assert.*;
import org.junit.Test;

public class BoundingBoxTest
{
    @Test
    public void testCreate()
    {
        BoundingBox bb = new BoundingBox(1, 2, 3, 4);
        assertEquals(bb.getTop(),    1);
        assertEquals(bb.getLeft(),   2);
        assertEquals(bb.getBottom(), 3);
        assertEquals(bb.getRight(),  4);
    }

    @Test
    public void testCreateFromVectors()
    {
        BoundingBox bb = new BoundingBox(Vectors.create(2, 1), Vectors.create(4, 3));
        assertEquals(bb.getTop(),    1);
        assertEquals(bb.getLeft(),   2);
        assertEquals(bb.getBottom(), 3);
        assertEquals(bb.getRight(),  4);
    }

    @Test
    public void testContainsTrue()
    {
        BoundingBox bbOuter = new BoundingBox(0, 0, 2, 2);
        BoundingBox bbInner = new BoundingBox(1, 1, 2, 2);
        assertTrue(bbOuter.contains(bbInner));
    }

    @Test
    public void testContainsUntrue()
    {
        BoundingBox bbOuter = new BoundingBox(0, 0, 2, 2);
        BoundingBox bbInner = new BoundingBox(1, 1, 2, 2);
        assertFalse(bbInner.contains(bbOuter));
    }

    @Test
    public void testIntersectsTrue()
    {
        BoundingBox bb1 = new BoundingBox(-1, -1, 1, 1);
        BoundingBox bb2 = new BoundingBox(0, 0, 2, 2);
        assertTrue(bb1.intersects(bb2));
    }

    @Test
    public void testIntersectsIsInclusive()
    {
        BoundingBox bb1 = new BoundingBox(-1, -1, 1, 1);
        BoundingBox bb2 = new BoundingBox(1, 1, 2, 2);
        assertTrue(bb1.intersects(bb2));
    }

    @Test
    public void testIntersectsFalse()
    {
        BoundingBox bb1 = new BoundingBox(-1, -1, 1, 1);
        BoundingBox bb2 = new BoundingBox(2, 2, 4, 4);
        assertFalse(bb1.intersects(bb2));
    }

    @Test
    public void testIntersection()
    {
        BoundingBox bbOuter = new BoundingBox(0, 0, 2, 2);
        BoundingBox bbInner = new BoundingBox(1, 1, 3, 3);
        BoundingBox intersection = bbOuter.intersection(bbInner);
        assertEquals(intersection.getTop(), 1);
        assertEquals(intersection.getLeft(), 1);
        assertEquals(intersection.getBottom(), 2);
        assertEquals(intersection.getRight(), 2);
    }
}
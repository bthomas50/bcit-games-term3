package sketchwars.physics;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class BitMaskFactoryTest
{
    @Test
    public void testCreateRectangleEasy()
    {
        BitMask bm = BitMaskFactory.createRectangle(new BoundingBox(0, 0, 0, 63));
        assertEquals(0xFFFFFFFFFFFFFFFFl, bm.getSubmaskElement(0, 0));
        assertEquals(new BoundingBox(0,0,0,63), bm.getBounds());
    }

    @Test
    public void testCreateRectangleWidthHeightEasy()
    {
        BitMask bm = BitMaskFactory.createRectangle(64, 1);
        assertEquals(0xFFFFFFFFFFFFFFFFl, bm.getSubmaskElement(0, 0));
        assertEquals(64, bm.getBounds().getWidth());
        assertEquals(1, bm.getBounds().getHeight());
    }

    @Test
    public void testCreateRectangleMisaligned()
    {
        BitMask bm = BitMaskFactory.createRectangle(new BoundingBox(0, 0, 0, 64));
        assertEquals(0xFFFFFFFFFFFFFFFFl, bm.getSubmaskElement(0, 0));
        assertEquals(0x8000000000000000l, bm.getSubmaskElement(0, 64));
    }

    @Test
    public void testCreateRectangleDoublyMisaligned()
    {
        BitMask bm = BitMaskFactory.createRectangle(new BoundingBox(0, 4, 0, 68));
        assertEquals(0xFFFFFFFFFFFFFFFFl, bm.getSubmaskElement(0, 0));
        assertEquals(0x8000000000000000l, bm.getSubmaskElement(0, 64));
    }

    @Test
    public void testCreateUnitCircle()
    {
        BitMask bm = BitMaskFactory.createCircle(1.0);
        bm.trim();
        assertEquals(new BoundingBox(0, 0, 2, 2), bm.getBounds());
        assertEquals(0x4000000000000000l, bm.getSubmaskElement(0, 0));
        assertEquals(0xE000000000000000l, bm.getSubmaskElement(1, 0));
        assertEquals(0x4000000000000000l, bm.getSubmaskElement(2, 0));
    }

    @Test
    public void testCreateOtherCircle()
    {
        BitMask bm = BitMaskFactory.createCircle(10.001);
        bm.trim();
        assertEquals(new BoundingBox(0, 0, 20, 21), bm.getBounds());
        assertEquals(0x0030000000000000l, bm.getSubmaskElement(0, 0));
        assertEquals(0xFFFFF80000000000l, bm.getSubmaskElement(6, 0));
        assertEquals(0x0070000000000000l, bm.getSubmaskElement(20, 0));
    }

    @Test
    public void testCreateThirdCircle()
    {
        BitMask bm = BitMaskFactory.createCircle(9.999);
        bm.trim();
        assertEquals(new BoundingBox(0, 0, 19, 20), bm.getBounds());
        assertEquals(0x0060000000000000l, bm.getSubmaskElement(0, 0));
        assertEquals(0xFFFFF80000000000l, bm.getSubmaskElement(6, 0));
        assertEquals(0x07FF000000000000l, bm.getSubmaskElement(19, 0));
    }
}
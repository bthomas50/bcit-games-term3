package sketchwars.physics;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class BitMaskTest
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
        dataWithExtraZeros[0][0] = 0x0000FFFFFFFF0000l;
        dataWithExtraZeros[1][0] = 0x00000FFFFFF00000l;
    }

    private BitMask rectMask;
    private BitMask irregMask;
    private BitMask wideMask;
    @Before
    public void initMasks()
    {
        rectMask = new BitMask(rectangularData);
        irregMask = new BitMask(irregularData);
        wideMask = new BitMask(dataWithExtraZeros);
    }

    @Test
    public void testCreateRectangular()
    {
        assertTrue(rectMask.getBounds().equals(new BoundingBox(0, 0, 1, 127)));
    }

    @Test
    public void testCreateIrregular()
    {
        assertTrue(irregMask.getBounds().equals(new BoundingBox(0, 0, 1, 63)));
    }

    @Test
    public void testCreateWide()
    {
        assertTrue(wideMask.getBounds().equals(new BoundingBox(0, 0, 1, 63)));
    }

    @Test
    public void testCreateEmpty()
    {
        BitMask empty = new BitMask(null);
        assertTrue(empty.isEmpty());
    }
	
	@Test
	public void testGetBounds()
	{
		rectMask.setPosition(Vectors.create(2, 3));
		assertEquals(new BoundingBox(3, 2, 4, 129), rectMask.getBounds());
	}
    @Test
    public void testGetArea()
    {
        assertEquals(rectMask.getArea(), 64 * 2 * 2);
    }

    @Test
    public void testTrimDoesntAffectFullData()
    {
        rectMask.trim();
        assertTrue(rectMask.getBounds().equals(new BoundingBox(0, 0, 1, 127)));
    }

    @Test
    public void testTrimReducesHeightCorrectly()
    {
        irregMask.trim();
        assertTrue(irregMask.getBounds().equals(new BoundingBox(0, 0, 0, 63)));
    }

    @Test
    public void testTrimReducesWidthCorrectly()
    {
        wideMask.trim();
        assertTrue(wideMask.getBounds().equals(new BoundingBox(0, 16, 1, 47)));
    }

    @Test
    public void testGetCOMRect()
    {
        long vCOM = rectMask.getCenterOfMass();
        assertEquals(63.5, Vectors.xComp(vCOM), Vectors.EPSILON);
        assertEquals(0.5, Vectors.yComp(vCOM), Vectors.EPSILON);
    }
	
	@Test
	public void testGetCOMWithOffset()
	{
		rectMask.setPosition(Vectors.create(1, 10));
		long vCOM = rectMask.getCenterOfMass();
        assertEquals(64.5, Vectors.xComp(vCOM), Vectors.EPSILON);
        assertEquals(10.5, Vectors.yComp(vCOM), Vectors.EPSILON);
	}

    @Test
    public void testGetCOMAfterTrim()
    {
        irregMask.trim();
        long vCOM = irregMask.getCenterOfMass();
        assertEquals(31.5, Vectors.xComp(vCOM), Vectors.EPSILON);
        assertEquals(0, Vectors.yComp(vCOM), Vectors.EPSILON);
    }

    @Test
    public void testGetCOMWide()
    {
        long vCOM = wideMask.getCenterOfMass();
        assertEquals(31.5, Vectors.xComp(vCOM), Vectors.EPSILON);
        assertEquals(6.0/14.0, Vectors.yComp(vCOM), Vectors.EPSILON);
    }

    @Test
    public void testANDTwoRects()
    {
        BitMask result = rectMask.and(rectMask);
        result.trim();
        assertTrue(result.getBounds().equals(new BoundingBox(0, 0, 1, 127)));
    }

    @Test
    public void testANDRectIrregular()
    {
        BitMask result = rectMask.and(irregMask);
        result.trim();
        assertTrue(result.getBounds().equals(new BoundingBox(0, 0, 0, 63)));
    }

    @Test
    public void testANDRectWide()
    {
        BitMask result = rectMask.and(wideMask);
        result.trim();
        assertEquals(new BoundingBox(0, 16, 1, 47), result.getBounds());
    }
	
	@Test
	public void testANDWithSameOffset()
	{
		rectMask.setPosition(Vectors.create(-1, 20));
		BitMask result = rectMask.and(rectMask);
		result.trim();
		assertEquals(new BoundingBox(20, -1, 21, 126), result.getBounds());
	}
	
	@Test
	public void testANDWithDifferentOffsets()
	{
		rectMask.setPosition(Vectors.create(20, 0));
		wideMask.setPosition(Vectors.create(12, -1));
		BitMask result = rectMask.and(wideMask);
		result.trim();
		long vExpectedPos = Vectors.create(20, 0);
		long vActualPos = result.getPosition();
		assertEquals(Vectors.ixComp(vExpectedPos), Vectors.ixComp(vActualPos));
		assertEquals(Vectors.iyComp(vExpectedPos), Vectors.iyComp(vActualPos));
		assertEquals(new BoundingBox(0, 32, 0, 55), result.getBounds());
	}
	
	@Test
	public void testANDWorksBothWays()
	{
		rectMask.setPosition(Vectors.create(20, 0));
		wideMask.setPosition(Vectors.create(12, -1));
		BitMask result = wideMask.and(rectMask);
		result.trim();
		long vExpectedPos = Vectors.create(20, 0);
		long vActualPos = result.getPosition();
		assertEquals(Vectors.ixComp(vExpectedPos), Vectors.ixComp(vActualPos));
		assertEquals(Vectors.iyComp(vExpectedPos), Vectors.iyComp(vActualPos));
		assertEquals(new BoundingBox(0, 32, 0, 55), result.getBounds());
	}

    @Test
    public void testGetSubmaskElementEasy()
    {
        long result = rectMask.getSubmaskElement(0, 0);
        assertEquals(0xFFFFFFFFFFFFFFFFl, result);
    }

    @Test
    public void testGetSubmaskElementNonzeroBitIdx()
    {
        long result = wideMask.getSubmaskElement(1, 4);
        assertEquals(0x0000FFFFFF000000l, result);
    }

    @Test public void testGetFirstBit()
    {
        long result = rectMask.getSubmaskElement(1, -63);
        assertEquals(0x0000000000000001l, result);
    }
    @Test public void testGetDataFromDifferentLongs()
    {
        long result = rectMask.getSubmaskElement(0, 32);
        assertEquals(0xFFFFFFFFFFFFFFFFl, result);
    }
    @Test public void testGetLastBit()
    {
        long result = rectMask.getSubmaskElement(1, 127);
        assertEquals(0x8000000000000000l, result);
    }

    @Test
    public void testGetSubmaskElementNegativeBitIdx()
    {
        long result = wideMask.getSubmaskElement(1, -4);
        assertEquals(0x000000FFFFFF0000l, result);
    }

}
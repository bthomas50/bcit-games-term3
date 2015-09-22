package Physics;

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
        rectangularData = new long[2][1];
        rectangularData[0][0] = 0xFFFFFFFFFFFFFFFFl;
        rectangularData[1][0] = 0xFFFFFFFFFFFFFFFFl;

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
        assertTrue(rectMask.getBounds().equals(new BoundingBox(0, 0, 1, 63)));
    }

    @Test
    public void testCreateIrregular()
    {
        assertTrue(rectMask.getBounds().equals(new BoundingBox(0, 0, 1, 63)));
    }

    @Test
    public void testCreateWide()
    {
        assertTrue(rectMask.getBounds().equals(new BoundingBox(0, 0, 1, 63)));
    }

    @Test
    public void testGetArea()
    {
        assertEquals(rectMask.getArea(), 64 * 2);
    }

    @Test
    public void testTrimDoesntAffectFullData()
    {
        rectMask.trim();
        assertTrue(rectMask.getBounds().equals(new BoundingBox(0, 0, 1, 63)));
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
        assertEquals(31.5, Vectors.xComp(vCOM), Vectors.EPSILON);
        assertEquals(0.5, Vectors.yComp(vCOM), Vectors.EPSILON);
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
}
package sketchwars.physics;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class CollisionsTest
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
    TestListener list;

    @Before
    public void initColliders()
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
    }

    @Test
    public void testHandleCollision()
    {
        Collisions.handle(rectCollider, irregCollider);
        assertEquals(2, list.collisions.size());
    }

    @Test
    public void testHandleNoCollision()
    {
        Collisions.handle(irregCollider, wideCollider);
        assertEquals(0, list.collisions.size());
    }
}
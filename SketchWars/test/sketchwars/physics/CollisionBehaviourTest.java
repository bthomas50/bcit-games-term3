package sketchwars.physics;

import static sketchwars.physics.CollisionBehaviour.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class CollisionBehaviourTest
{
    @Test
    public void testMinNone()
    {
        assertEquals(NONE, min(NONE, NONE));
        assertEquals(NONE, min(NONE, NOTIFY));
        assertEquals(NONE, min(NONE, CLIP));
        assertEquals(NONE, min(NONE, TRANSFER_MOMENTUM));
    }
    @Test
    public void testMinNotify()
    {
        assertEquals(NOTIFY, min(NOTIFY, NOTIFY));
        assertEquals(NOTIFY, min(NOTIFY, CLIP));
        assertEquals(NOTIFY, min(NOTIFY, TRANSFER_MOMENTUM));
    }
    @Test
    public void testMinClip()
    {
        assertEquals(CLIP, min(CLIP, CLIP));
        assertEquals(CLIP, min(CLIP, TRANSFER_MOMENTUM));
    }
    @Test
    public void testMinXfer()
    {
        assertEquals(TRANSFER_MOMENTUM, min(TRANSFER_MOMENTUM, TRANSFER_MOMENTUM));
    }
    @Test
    public void testIncludes()
    {
        assertTrue(NONE.includes(NONE));
        assertTrue(NOTIFY.includes(NONE));
        assertTrue(NOTIFY.includes(NOTIFY));
        assertTrue(CLIP.includes(NONE));
        assertTrue(CLIP.includes(NOTIFY));
        assertTrue(CLIP.includes(CLIP));
        assertTrue(TRANSFER_MOMENTUM.includes(NONE));
        assertTrue(TRANSFER_MOMENTUM.includes(NOTIFY));
        assertTrue(TRANSFER_MOMENTUM.includes(CLIP));
        assertTrue(TRANSFER_MOMENTUM.includes(TRANSFER_MOMENTUM));
    }
}
package sketchwars.util;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;


public class Phys2ImageTest
{
    private CoordinateTransform transformer;

    @Before
    public void createTransform()
    {
        transformer = new PhysicsToImage(new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB));
    }

    @Test
    public void testTransformX()
    {
        assertEquals(0.0f, transformer.transformX(-1024), 0.001f);
        assertEquals(128.0f, transformer.transformX(0), 0.001f);
        assertEquals(256.0f, transformer.transformX(1024), 0.001f);
    }

    @Test
    public void testTransformY()
    {
        assertEquals(0.0f, transformer.transformY(1024), 0.001f);
        assertEquals(128.0f, transformer.transformY(0), 0.001f);
        assertEquals(256.0f, transformer.transformY(-1024), 0.001f);
    }

    @Test
    public void testTransformWidth()
    {
        assertEquals(0.0f, transformer.transformWidth(0), 0.001f);
        assertEquals(128.0f, transformer.transformWidth(1024), 0.001f);
        assertEquals(256.0f, transformer.transformWidth(2048), 0.001f);
    }

    @Test
    public void testTransformHeight()
    {
        assertEquals(0.0f, transformer.transformHeight(0), 0.001f);
        assertEquals(128.0f, transformer.transformHeight(1024), 0.001f);
        assertEquals(256.0f, transformer.transformHeight(2048), 0.001f);
    }


    @Test
    public void testInvTransformX()
    {
        assertEquals(-1024.0f, transformer.invTransformX(0), 0.001f);
        assertEquals(0.0f, transformer.invTransformX(128), 0.001f);
        assertEquals(1024.0f, transformer.invTransformX(256), 0.001f);
    }

    @Test
    public void testInvTransformY()
    {
        assertEquals(1024.0f, transformer.invTransformY(0), 0.001f);
        assertEquals(0.0f, transformer.invTransformY(128), 0.001f);
        assertEquals(-1024.0f, transformer.invTransformY(256), 0.001f);
    }

    @Test
    public void testInvTransformWidth()
    {
        assertEquals(0.0f, transformer.invTransformWidth(0), 0.001f);
        assertEquals(1024.0f, transformer.invTransformWidth(128), 0.001f);
        assertEquals(2048.0f, transformer.invTransformWidth(256), 0.001f);
    }

    @Test
    public void testInvTransformHeight()
    {
        assertEquals(0.0f, transformer.invTransformHeight(0), 0.001f);
        assertEquals(1024.0f, transformer.invTransformHeight(128), 0.001f);
        assertEquals(2048.0f, transformer.invTransformHeight(256), 0.001f);
    }
}
package sketchwars.physics;

import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.Math;

public class VectorsTest
{
    @Test
    public void testCreateFromInts()
    {
        long v = Vectors.create(1, 10);
        assertEquals(Vectors.ixComp(v), 1);
        assertEquals(Vectors.iyComp(v), 10);
    }
    @Test
    public void testCreateFromNegativeInts()
    {
        long v = Vectors.create(-1, -10);
        assertEquals(Vectors.ixComp(v), -1);
        assertEquals(Vectors.iyComp(v), -10);
    }
    @Test
    public void testCreateFromWholeDoubles()
    {
        long v = Vectors.create(-1.0, 2.0);
        assertEquals(Vectors.ixComp(v), -1);
        assertEquals(Vectors.iyComp(v), 2);
    }
    @Test
    public void testCreateFromFractionalDoubles()
    {
        long v = Vectors.create(0.5, -2.5);
        assertEquals(Vectors.ixComp(v), 0);
        assertEquals(Vectors.iyComp(v), -2);
        assertEquals(Vectors.xComp(v), 0.5, Vectors.EPSILON);
        assertEquals(Vectors.yComp(v), -2.5, Vectors.EPSILON);
    }
    @Test
    public void testCreateFromCloseToWholeDoubles()
    {
        long v = Vectors.create(0.999, -0.999);
        assertEquals(Vectors.ixComp(v), 0);
        assertEquals(Vectors.iyComp(v), 0);
        assertEquals(Vectors.xComp(v), 0.999, Vectors.EPSILON);
        assertEquals(Vectors.yComp(v), -0.999, Vectors.EPSILON);
    }
    @Test
    public void testCreateRTheta()
    {
        long v = Vectors.createRTheta(1.0, Math.PI / 2.0);
        assertEquals(Vectors.xComp(v), 0.0, Vectors.EPSILON);
        assertEquals(Vectors.yComp(v), 1.0, Vectors.EPSILON);
    }
    @Test
    public void testAdd()
    {
        long v1 = Vectors.create(1.0, 2.0);
        long v2 = Vectors.create(-2.0, 1.0);
        long vSum = Vectors.add(v1, v2);
        assertEquals(Vectors.xComp(vSum), -1.0, Vectors.EPSILON);
        assertEquals(Vectors.yComp(vSum), 3.0, Vectors.EPSILON);
    }
    @Test
    public void testSubtract()
    {
        long v1 = Vectors.create(1.0, 2.0);
        long v2 = Vectors.create(-2.0, 1.0);
        long vDiff = Vectors.subtract(v1, v2);
        assertEquals(Vectors.xComp(vDiff), 3.0, Vectors.EPSILON);
        assertEquals(Vectors.yComp(vDiff), 1.0, Vectors.EPSILON);
    }
    @Test
    public void testReverse()
    {
        long v = Vectors.create(1.0, -2.0);
        assertEquals(Vectors.xComp(Vectors.reverse(v)), -1.0, Vectors.EPSILON);
        assertEquals(Vectors.yComp(Vectors.reverse(v)), 2.0, Vectors.EPSILON);
    }
    @Test
    public void testPerp()
    {
        long v = Vectors.create(1.0, -2.0);
        assertEquals(2.0, Vectors.xComp(Vectors.perpendicular(v)), Vectors.EPSILON);
        assertEquals(1.0, Vectors.yComp(Vectors.perpendicular(v)), Vectors.EPSILON);
    }
    @Test
    public void testDot()
    {
        long v1 = Vectors.create(1.0, 3.0);
        long v2 = Vectors.create(2, 2);
        assertEquals(Vectors.dot(v1, v2), 2 + 6, Vectors.EPSILON);
    }
    @Test
    public void testScalarMultiply()
    {
        long v = Vectors.create(0.0, 5.0);
        long vResult = Vectors.scalarMultiply(v, -2.0);
        assertEquals(Vectors.xComp(vResult), 0.0, Vectors.EPSILON);
        assertEquals(Vectors.yComp(vResult), -10.0, Vectors.EPSILON);
    }
    @Test
    public void testLength()
    {
        long v = Vectors.create(3, 4);
        assertEquals(Vectors.length(v), 5.0, Vectors.EPSILON);
    }
    @Test
    public void testDirectionQ1()
    {
        long v = Vectors.create(1, 1);
        assertEquals(Vectors.direction(v), Math.PI / 4.0, Vectors.EPSILON);
    }
    @Test
    public void testDirectionQ2()
    {
        long v = Vectors.create(-1, 1);
        assertEquals(Vectors.direction(v), 3.0 * Math.PI / 4.0, Vectors.EPSILON);
    }
    @Test
    public void testDirectionQ3()
    {
        long v = Vectors.create(-1, -1);
        assertEquals(Vectors.direction(v), -3.0 * Math.PI / 4.0, Vectors.EPSILON);
    }
    @Test
    public void testDirectionQ4()
    {
        long v = Vectors.create(1, -1);
        assertEquals(Vectors.direction(v), -Math.PI / 4.0, Vectors.EPSILON);
    }
    @Test
    public void testNormalize()
    {
        long v = Vectors.create(3, 4);
        long vResult = Vectors.normalize(v);
        assertEquals(Vectors.xComp(vResult), 0.6, Vectors.EPSILON);
        assertEquals(Vectors.yComp(vResult), 0.8, Vectors.EPSILON);
    }
    @Test
    public void testScaleToLength()
    {
        long v = Vectors.create(3, -4);
        long vResult = Vectors.scaleToLength(v, 10.0);
        assertEquals(Vectors.xComp(vResult), 6, Vectors.EPSILON);
        assertEquals(Vectors.yComp(vResult), -8, Vectors.EPSILON);
    }
    @Test
    public void testScaleToNegativeLength()
    {
        long v = Vectors.create(3, -4);
        long vResult = Vectors.scaleToLength(v, -10.0);
        assertEquals(Vectors.xComp(vResult), 6, Vectors.EPSILON);
        assertEquals(Vectors.yComp(vResult), -8, Vectors.EPSILON);
    }
    @Test
    public void testProjection()
    {
        long vTarget = Vectors.create(10, 0);
        long vToProject = Vectors.create(1, 1);
        long vProjection = Vectors.projection(vToProject, vTarget);
        assertEquals(Vectors.xComp(vProjection), 1.0, Vectors.EPSILON);
        assertEquals(Vectors.yComp(vProjection), 0.0, Vectors.EPSILON);
    }
    @Test
    public void testCap()
    {
        long v = Vectors.create(1.5, 5.5);
        long vResult = Vectors.cap(v, 3);
        assertEquals(Vectors.xComp(vResult), 1.5, Vectors.EPSILON);
        assertEquals(Vectors.yComp(vResult), 3.0, Vectors.EPSILON);
    }
}
package sketchwars.physics;

import java.lang.Math;
/**
 * Static methods for dealing with Vector primitives. Vectors will be stored as 2 32-bit ints packed into a 64-bit int.
 * @author Brian
 */
public class Vectors
{
    //Number of decimal places we can represent.
    public static final long POINT = 6L;
    //The precision of these values.
    public static final double EPSILON = 1.0 / 64.0;
    //For unpacking.
    private static final long INT_MASK = 0xFFFFFFFFL;
    //For multiplying to/from fixed-point doubles
    private static final long FACTOR = 64L;

    private static final long INT_OFFSET = 32L;

    //useful constants!
    public static final long V_ZERO = create(0,0);
    public static final long V_UNIT_X = create(1,0);
    public static final long V_UNIT_Y = create(0,1);

    
    public static long create(double x, double y)
    {
        return pack((int)(x * FACTOR), (int)(y * FACTOR));
    }

    public static long create(int x, int y)
    {
        return pack((x<<POINT),(y<<POINT));
    }

    public static long createRTheta(double r, double theta)
    {
        return create(r * Math.cos(theta), r * Math.sin(theta));
    }

    public static double xComp(long v)
    {
        return ((double)((v>>INT_OFFSET))) / FACTOR;
    }

    public static double yComp(long v)
    {
        return ((double)((int)(v & INT_MASK))) / FACTOR;
    }

    public static int ixComp(long v)
    {
        return (int)(((v>>INT_OFFSET)) / (int)FACTOR);
    }

    public static int iyComp(long v)
    {
        return ((int)(v & INT_MASK)) / (int)FACTOR;
    }

    public static long add(long u, long v)
    {
        return pack(left(u) + left(v), right(u) + right(v));
    }

    public static long subtract(long u, long v)
    {
        return pack(left(u) - left(v), right(u) - right(v));
    }

    public static long reverse(long v)
    {
        return pack(-left(v), -right(v));
    }
    //rotate 90 degrees CCW
    public static long perpendicular(long v)
    {
        return pack(-right(v), left(v));
    }

    //get the dot product as a double
    public static double dot(long u, long v)
    {
        double result = (double)(left(u)) / FACTOR * left(v) / FACTOR;
        result += (double)(right(u)) / FACTOR * right(v) / FACTOR;
        return result;
    }
    
    public static long scalarMultiply(double sca, long v)
    {
        return pack((int)(left(v) * sca), (int)(right(v) * sca));
    }

    public static long scalarMultiply(long v, double sca)
    {
        return scalarMultiply(sca, v);
    }
    
    //aka magnitude
    public static double length(long v)
    {
        double x = xComp(v);
        double y = yComp(v);
        return Math.sqrt(x * x + y * y);
    }

    //get the direction, in radians
    public static double direction(long v)
    {
        double x = xComp(v);
        double y = yComp(v);
        return Math.atan2(y, x);
    }

    //Get a new vector with length 1, and the same direction
    public static long normalize(long v)
    {
        return scalarMultiply(1.0 / length(v), v);
    }
    
    //Get a new vector with the given length, and the same direction
    public static long scaleToLength(double sca, long v)
    {
        return scalarMultiply(Math.abs(sca) / length(v), v);
    }

    //Get a new vector with the given length, and the same direction
    public static long scaleToLength(long v, double sca)
    {
        return scaleToLength(sca, v);
    }

    //get the projectection of U onto V (a vector)
    public static long projection(long u, long v)
    {
        long num = dotTimesFactorSquared(u,v);
        long denom = dotTimesFactorSquared(v,v);
        return scalarMultiply((double)num/denom, v);
    }
    
    //get a new vector with the magnitude of each component clamped to be less than or equal to max
    public static long cap(long v, int max)
    {
        if(left(v) > max<<POINT)
        {
            v = pack(max<<POINT, right(v));
        }
        else if (left(v) < -(max<<POINT))
        {
            v = pack(-(max<<POINT), right(v));
        }
        if(right(v) > max<<POINT)
        {
            v = pack(left(v), max<<POINT);
        }
        else if (right(v) < -(max<<POINT))
        {
            v = pack(left(v), -(max<<POINT));
        }
        return v;
    }

    public static String toString(long v)
    {
        return xComp(v) + ", " + yComp(v);
    }

    //get the bits for the x component
    private static int left(long v)
    {
        return ((int)(v>>INT_OFFSET));
    }

    //get the bits for the y component
    private static int right(long v)
    {
        return ((int) (v & INT_MASK));
    }
    
    //get the dotTimesFactorSquared product as an integer. (NOT TO SCALE)
    private static long dotTimesFactorSquared(long u, long v)
    {
        return (left(u) * left(v)) + (right(u) * right(v));
    }

    //pack 2 32-bit ints into a 64-bit long
    private static long pack(int x, int y)
    {
        long xPacked = ((long)(x))<<INT_OFFSET;
        long yPacked = (long)(y) & INT_MASK;
        return xPacked | yPacked;
    }

    private Vectors()
    {}
}

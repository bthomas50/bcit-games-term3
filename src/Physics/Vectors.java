package Physics;


/**
 * Static methods for dealing with Vector primitives. Vectors will be stored as 2 32-bit ints packed into a 64-bit int.
 * @author Brian
 */
public class Vectors
{
    /**
     * For unpacking.
     */
    private static final long MASK = 0xFFFFFFFFL;
    /**
     * For converting to/from fixed-point integers.
     */
    private static final long POINT = 6L;
    /**
     * For multiplying to/from fixed-point doubles.
     */
    private static final long FACTOR = 64L;
    /**
     * Not an object.
     */
    private Vectors()
    {
    }
    
    /**
     * Add a vector to another.
     * @param u A vector.
     * @param v A vector.
     * @return u + v
     */
    public static long add(long u, long v)
    {
        return pack(left(u) + left(v), right(u) + right(v));
    }
    /**
     * Subtract a vector from another.
     * @param u A vector.
     * @param v A vector.
     * @return u - v
     */
    public static long subtract(long u, long v)
    {
        return pack(left(u) - left(v), right(u) - right(v));
    }
    /**
     * Invert a vector.
     * @param v A vector.
     * @return -v
     */
    public static long reverse(long v)
    {
        return pack(-left(v), -right(v));
    }
     
    /**
     * Dot product of 2 vectors.
     * @param u A vector.
     * @param v A vector.
     * @return u dot v (a scalar)
     */
    private static long dot(long u, long v)
    {
        return (left(u) * left(v)) + (right(u) * right(v));
    }
    /**
     * Dot product of 2 vectors.
     * @param u A vector.
     * @param v A vector.
     * @return u dot v (a scalar)
     */
    public static double ddot(long u, long v)
    {
        double result = (double)(left(u)) / FACTOR * left(v) / FACTOR;
        result += (double)(right(u)) / FACTOR * right(v) / FACTOR;
        return result;
    }
    
    /**
     * Multiply a vector by a scalar.
     * @param sca
     * @param v A vector.
     * @return sca * v
     */
    public static long scalarMultiply(double sca, long v)
    {
        return pack((int)(left(v) * sca), (int)(right(v) * sca));
    }
    
    /**
     * Normalize a vector.
     * @param v A vector.
     * @return v, but with 1 length.
     */
    public static long normalize(long v)
    {
        return scalarMultiply(1.0 / length(v), v);
    }
    
    /**
     * Scale a vector to any length.
     * @param sca
     * @param v A vector.
     * @return v, but with sca length.
     */
    public static long scaleToLength(double sca, long v)
    {
        return scalarMultiply(sca / length(v), v);
    }
    
    /**
     * The length of a vector.
     * @param v A vector.
     * @return length of v.
     */
    public static double length(long v)
    {
        double x = xComp(v);
        double y = yComp(v);
        return Math.sqrt(x * x + y * y);
    }
    /**
     * Get direction of a vector.
     * @param v a vector
     * @return direction of v (in radians).
     */
    public static double direction(long v)
    {
        double x = xComp(v);
        double y = yComp(v);
        return Math.atan2(y, x);
    }
    /**
     * Projection of a vector onto another.
     * @param u A vector.
     * @param v A vector.
     * @return u projected onto v.
     */
    public static long projection(long u, long v)
    {
        long num = dot(u,v);
        long denom = dot(v,v);
        return scalarMultiply((double)num/denom, v);
    }
    
    /**
     * Cap the values of a vector.
     * @param v A vector.
     * @param max
     * @return v, with values capped to +/- max
     */
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
    
    /**
     * The x component
     * @param v A vector.
     * @return
     */
    private static int left(long v)
    {
        return ((int)(v>>32));
    }
    
    /**
     * The y component
     * @param v A vector.
     * @return
     */
    private static int right(long v)
    {
        return ((int) (v & MASK));
    }
    
    /**
     * Create a vector from shifted ints.
     * @param x
     * @param y
     * @return (x, y)
     */
    private static long pack(int x, int y)
    {
        long xPacked = ((long)(x))<<32;
        long yPacked = (long)(y) & MASK;
        return xPacked | yPacked;
    }
    
    /**
     * Get x component of a vector (as a double)
     * @param v A vector.
     * @return v.x
     */
    public static double xComp(long v)
    {
        return ((double)((v>>32))) / 64;
    }
    /**
     * Get y component of a vector (as a double)
     * @param v A vector.
     * @return v.y
     */
    public static double yComp(long v)
    {
        return ((double)((int)(v & MASK))) / 64;
    }
    /**
     * Get x component of a vector (as an int)
     * @param v A vector.
     * @return v.x
     */
    public static int ixComp(long v)
    {
        return (int)(((v>>32)) / 64);
    }
    /**
     * Get y component of a vector (as an int)
     * @param v A vector.
     * @return v.y
     */
    public static int iyComp(long v)
    {
        return ((int)(v & MASK)) / 64;
    }
    /**
     * Create a vector from doubles.
     * @param x
     * @param y
     * @return (x, y)
     */
    public static long create(double x, double y)
    {
        return pack((int)(x * 64), (int)(y * 64));
    }
    /**
     * Create a vector from ints.
     * @param x
     * @param y
     * @return (x, y)
     */
    public static long create(int x, int y)
    {
        return pack((x<<POINT),(y<<POINT));
    }
    /**
     * Create a vector from magnitude and direction.
     * @param r magnitude
     * @param theta direction (radians)
     * @return (x,y) vector
     */
    public static long createRTheta(double r, double theta)
    {
        return create(r * Math.cos(theta), r * Math.sin(theta));
    }
    /**
     * print a vector.
     * @param v A vector.
     * @return string of v.
     */
    public static String toString(long v)
    {
        return xComp(v) + ", " + yComp(v);
    }
}

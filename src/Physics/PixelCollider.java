package Physics;

/**
 * Class of physics objects that collides based on the intersection of pixel masks
 * @author Brian
 */
public class PixelCollider
{
    private BitMask bitMask;
    private long vPosition;

    public PixelCollider(BitMask bm)
    {
        bitMask = bm;
        vPosition = Vectors.create(0,0);
    }
}
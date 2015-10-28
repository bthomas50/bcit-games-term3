package sketchwars.physics;

public class MapCollider extends PixelCollider
{
    public MapCollider(BitMask bm)
    {
        super(bm);
    }

    public MapCollider(BitMask bm, CollisionBehaviour behaviour)
    {
        super(bm, behaviour);
    }

    public void setPixels(BitMask bm)
    {
        bitMask = bm;
    }
}
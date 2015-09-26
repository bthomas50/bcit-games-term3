package sketchwars.physics;

public class Collisions
{
    public static void handle(Collider coll1, Collider coll2)
    {
        //we only have PixelColliders for now
        BitMask mask1 = ((PixelCollider) coll1).getPixels();
        BitMask mask2 = ((PixelCollider) coll2).getPixels();
        BitMask result = mask1.and(mask2);
        if(!result.isEmpty())
        {
            coll1.notify(coll2);
            coll2.notify(coll1);
        }
    }

    private Collisions() 
    {}
}
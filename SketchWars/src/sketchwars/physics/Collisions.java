package sketchwars.physics;

import static sketchwars.physics.Vectors.*;

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
            System.out.println("collision: " + coll1 + ", " + coll2);
            long vVel1 = coll1.getVelocity();
            long vVel2 = coll2.getVelocity();
            System.out.println(mask1.getBounds() + ":: " + mask2.getBounds());
            System.out.println(Vectors.toString(vVel1) + ":: " + Vectors.toString(vVel2));
            long clipVector = subtract(vVel1, vVel2);
            double clipDistance = result.getProjectedLength(clipVector) + 0.1;
            System.out.println(clipDistance);
            clipVector = normalize(clipVector);
            System.out.println(Vectors.toString(clipVector));
            double massFraction1 = coll1.getMass() / (coll1.getMass() + coll2.getMass());
            double massFraction2 = coll2.getMass() / (coll1.getMass() + coll2.getMass());
            coll1.setPosition(add(coll1.getPosition(), scalarMultiply(clipDistance * -massFraction1, clipVector)));
            coll2.setPosition(add(coll2.getPosition(), scalarMultiply(clipDistance * massFraction2, clipVector)));
            coll1.notify(coll2);
            coll2.notify(coll1);
        }
    }

    private Collisions() 
    {}
}
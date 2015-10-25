package sketchwars.physics;

import static sketchwars.physics.Vectors.*;
import static sketchwars.physics.CollisionBehaviour.*;
public class Collisions
{
    private static class CollisionData
    {
        private long vTranslation1, vTranslation2, vAcceleration1, vAcceleration2;
    }

    private static CollisionData collisionDataHolder = new Collisions.CollisionData();

    public static boolean hasCollided(Collider coll1, Collider coll2) {
        BitMask mask1 = ((PixelCollider) coll1).getPixels();
        BitMask mask2 = ((PixelCollider) coll2).getPixels();
        BitMask collision = mask1.and(mask2);  
        
        return (!collision.isEmpty());
    }
            
    
    public static void handle(Collider coll1, Collider coll2)
    {
        CollisionBehaviour behaviour = min(coll1.getCollisionBehaviour(coll2), coll2.getCollisionBehaviour(coll1));
        if(behaviour == NONE)
        {
            //nothing to do.
            return;
        }
        //we only have PixelColliders for now
        BitMask mask1 = ((PixelCollider) coll1).getPixels();
        BitMask mask2 = ((PixelCollider) coll2).getPixels();
        BitMask collision = mask1.and(mask2);
        if(!collision.isEmpty())
        {
            if(behaviour.includes(NOTIFY))
            {
                coll1.notify(coll2);
                coll2.notify(coll1);
            }
            if(behaviour.includes(CLIP) && !(coll1.isStatic() && coll2.isStatic()))
            {
                long vNorm1 = Vectors.normalize(mask1.getAverageNormal(collision));
                long vNorm2 = Vectors.normalize(mask2.getAverageNormal(collision));
                clip(coll1, coll2, collision, vNorm1, vNorm2);
                if(behaviour.includes(TRANSFER_MOMENTUM))
                {
                    transferMomentum(coll1, coll2, vNorm1, vNorm2);
                    applyResults(coll1, coll2);
                }
            }
        }
    }

    public static void clip(Collider coll1, Collider coll2, BitMask collision, long vNorm1, long vNorm2)
    {
        double clipDistance1 = collision.getProjectedLength(vNorm1);
        double clipDistance2 = collision.getProjectedLength(vNorm2);
        if(coll1.isStatic())
        {
            collisionDataHolder.vTranslation1 = 0;
            collisionDataHolder.vTranslation2 = scalarMultiply(clipDistance1, vNorm1);
        }
        else if(coll2.isStatic())
        {
            collisionDataHolder.vTranslation2 = 0;
            collisionDataHolder.vTranslation1 = scalarMultiply(clipDistance2, vNorm2);
        }
        else
        {
            double massFraction1 = coll1.getMass() / (coll1.getMass() + coll2.getMass());
            double massFraction2 = coll2.getMass() / (coll1.getMass() + coll2.getMass());
            if(clipDistance1 < clipDistance2)
            {
                collisionDataHolder.vTranslation1 = scalarMultiply(clipDistance1 * -massFraction2, vNorm1);
                collisionDataHolder.vTranslation2 = scalarMultiply(clipDistance1 * massFraction1, vNorm1);
            }
            else
            {
                collisionDataHolder.vTranslation1 = scalarMultiply(clipDistance2 * massFraction2, vNorm2);
                collisionDataHolder.vTranslation2 = scalarMultiply(clipDistance2 * -massFraction1, vNorm2);
            }
        }
    }

    public static void transferMomentum(Collider coll1, Collider coll2, long vNorm1, long vNorm2)
    {
        long vVel1 = coll1.getVelocity();
        long vVel2 = coll2.getVelocity();
        long vTransfer1To2 = projection(vVel1, vNorm1);
        if(dot(vVel1, vNorm1) < 0.0)
        {
            vTransfer1To2 = 0;
        }
        long vTransfer2To1 = projection(vVel2, vNorm2);
        if(dot(vVel2, vNorm2) < 0.0)
        {
            vTransfer2To1 = 0;
        }
        double elasticityCoeff = coll1.getElasticity() * coll2.getElasticity() + 1.0;

        if(coll1.isStatic())
        {
            collisionDataHolder.vAcceleration1 = 0;
            collisionDataHolder.vAcceleration2 = scalarMultiply(vTransfer2To1, -elasticityCoeff);
        }
        else if(coll2.isStatic())
        {
            collisionDataHolder.vAcceleration2 = 0;
            collisionDataHolder.vAcceleration1 = scalarMultiply(vTransfer1To2, -elasticityCoeff);
        }
        else
        {
            double massFraction1 = coll1.getMass() / (coll1.getMass() + coll2.getMass());
            double massFraction2 = coll2.getMass() / (coll1.getMass() + coll2.getMass());
            collisionDataHolder.vAcceleration1 = 0;
            collisionDataHolder.vAcceleration2 = 0;
            collisionDataHolder.vAcceleration1 = add(collisionDataHolder.vAcceleration1, scalarMultiply(vTransfer1To2, -elasticityCoeff * massFraction2));
            collisionDataHolder.vAcceleration2 = add(collisionDataHolder.vAcceleration2, scalarMultiply(vTransfer1To2, elasticityCoeff * massFraction1));
            collisionDataHolder.vAcceleration1 = add(collisionDataHolder.vAcceleration1, scalarMultiply(vTransfer2To1, elasticityCoeff * massFraction2));
            collisionDataHolder.vAcceleration2 = add(collisionDataHolder.vAcceleration2, scalarMultiply(vTransfer2To1, -elasticityCoeff * massFraction1));
        }
    }

    public static void applyResults(Collider coll1, Collider coll2)
    {
        coll1.setPosition(add(coll1.getPosition(), collisionDataHolder.vTranslation1));
        coll2.setPosition(add(coll2.getPosition(), collisionDataHolder.vTranslation2));
        coll1.setVelocity(add(coll1.getVelocity(), collisionDataHolder.vAcceleration1));
        coll2.setVelocity(add(coll2.getVelocity(), collisionDataHolder.vAcceleration2));
    }

    private Collisions() 
    {}
}
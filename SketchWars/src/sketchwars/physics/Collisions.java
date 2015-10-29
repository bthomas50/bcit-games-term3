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

    public static boolean hasCollided(Collider coll1, Collider coll2) 
    {
        return !getCollision(coll1, coll2).isEmpty();
    }
            
    
    public static void handle(Collider coll1, Collider coll2)
    {
        CollisionBehaviour behaviour = min(coll1.getCollisionBehaviour(coll2), coll2.getCollisionBehaviour(coll1));
        if(behaviour == NONE)
        {
            //nothing to do.
            return;
        }
        BitMask mask1 = ((PixelCollider)coll1).getPixels();
        BitMask mask2 = ((PixelCollider)coll2).getPixels();
        BitMask collision = getCollision(coll1, coll2);
        if(!collision.isEmpty())
        {
            resetData();
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
                    MomentumTransferResult result = transferMomentum(coll1, coll2, vNorm1, vNorm2);
                    collisionDataHolder.vAcceleration1 = result.vAcceleration1;
                    collisionDataHolder.vAcceleration2 = result.vAcceleration2;
                    FrictionResult fResult = applyFriction(coll1, coll2, result.vNormal, result.impulse);
                    collisionDataHolder.vAcceleration1 = add(collisionDataHolder.vAcceleration1, fResult.vAcceleration1);
                    collisionDataHolder.vAcceleration2 = add(collisionDataHolder.vAcceleration2, fResult.vAcceleration2);
                }
                applyResults(coll1, coll2);
            }
        }
    }

    public static BitMask getCollision(Collider coll1, Collider coll2) 
    {
        BitMask mask1 = ((PixelCollider) coll1).getPixels();
        BitMask mask2 = ((PixelCollider) coll2).getPixels();
        return mask1.and(mask2);
    }

    private static void resetData()
    {
        collisionDataHolder.vTranslation1 = 0;
        collisionDataHolder.vTranslation2 = 0;
        collisionDataHolder.vAcceleration1 = 0;
        collisionDataHolder.vAcceleration2 = 0;
    }

    public static void clip(Collider coll1, Collider coll2, BitMask collision, long vNorm1, long vNorm2)
    {
        if(coll1.isStatic())
        {
            double clipDistance1 = findSmallestAcceptableClipDistance(coll1, coll2, collision, vNorm1);
            double clipDistance2 = findSmallestAcceptableClipDistance(coll1, coll2, collision, reverse(vNorm2));
            if(clipDistance1 > 50)
            {
                System.out.println(clipDistance1 + ", " + clipDistance2);
                System.out.println(collision.getBounds());
                System.out.println(Vectors.toString(vNorm1) + "__" + Vectors.toString(vNorm2));
            }
            if(clipDistance1 < clipDistance2)
            {
                collisionDataHolder.vTranslation2 = scalarMultiply(clipDistance1, vNorm1);
            }
            else
            {
                collisionDataHolder.vTranslation2 = scalarMultiply(clipDistance2, reverse(vNorm2));
            }
        }
        else if(coll2.isStatic())
        {
            double clipDistance1 = findSmallestAcceptableClipDistance(coll2, coll1, collision, reverse(vNorm1));
            double clipDistance2 = findSmallestAcceptableClipDistance(coll2, coll1, collision, vNorm2);
            if(clipDistance1 < clipDistance2)
            {
                collisionDataHolder.vTranslation1 = scalarMultiply(clipDistance1, reverse(vNorm1));
            }
            else
            {
                collisionDataHolder.vTranslation1 = scalarMultiply(clipDistance2, vNorm2);
            }
        }
        else
        {
            double clipDistance1 = collision.getProjectedLength(vNorm1) + 0.5;
            double clipDistance2 = collision.getProjectedLength(vNorm2) + 0.5;
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

    private static double findSmallestAcceptableClipDistance(Collider staticColl, Collider dynamicColl, BitMask collision, long vDirection) 
    {
        long vSavedPos = dynamicColl.getPosition();
        double maxClip = collision.getProjectedLength(vDirection) + 0.5;
        for(double test = 1.0; test <= maxClip - 1; test += 1.0)
        {
            long vTranslation = scalarMultiply(test, vDirection);
            dynamicColl.setPosition(add(vSavedPos, vTranslation));
            if(!hasCollided(staticColl, dynamicColl))
            {
                dynamicColl.setPosition(vSavedPos);
                return test + 0.5;
            }
        }
        dynamicColl.setPosition(vSavedPos);
        return maxClip;
    }   

    public static MomentumTransferResult transferMomentum(Collider coll1, Collider coll2, long vNorm1, long vNorm2)
    {
        MomentumTransferResult ret = new MomentumTransferResult();
        long vRelative = subtract(coll2.getVelocity(), coll1.getVelocity());
        ret.vNormal = normalize(subtract(vNorm1, vNorm2));
        double normalVelocity = dot(vRelative, ret.vNormal);
        if(normalVelocity > 0)
        {
            return ret;
        }
        double elasticityCoeff = coll1.getElasticity() * coll2.getElasticity() + 1.0;
        double invMass1 = getInvMass(coll1);
        double invMass2 = getInvMass(coll2);
        ret.impulse = -normalVelocity * elasticityCoeff / (invMass1 + invMass2);
        ret.vAcceleration1 = scalarMultiply(ret.vNormal, -invMass1 * ret.impulse);
        ret.vAcceleration2 = scalarMultiply(ret.vNormal, invMass2 * ret.impulse);
        return ret;
    }

    private static double getInvMass(Collider coll) 
    {
        if(coll.isStatic())
        {
            return 0;
        }
        else
        {
            return 1.0 / coll.getMass();
        }
    }

    public static FrictionResult applyFriction(Collider coll1, Collider coll2, long vNormal, double momImpulse)
    {
        FrictionResult ret = new FrictionResult();
        long vRelative = subtract(coll2.getVelocity(), coll1.getVelocity());
        ret.vTangent = perpendicular(vNormal);
        double invMass1 = getInvMass(coll1);
        double invMass2 = getInvMass(coll2);
        ret.impulse = dot(vRelative, ret.vTangent) / (invMass1 + invMass2);
        if(Math.abs(ret.impulse) >= Math.abs(momImpulse) * 0.1)
        {
            System.out.println(ret.impulse + ", " + momImpulse);
            ret.impulse = Math.abs(momImpulse) * Math.signum(ret.impulse) * 0.03;
        }
        ret.vAcceleration1 = scalarMultiply(ret.vTangent, invMass1 * ret.impulse);
        ret.vAcceleration2 = scalarMultiply(ret.vTangent, -invMass2 * ret.impulse);
        return ret;
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
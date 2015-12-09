package sketchwars.physics;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;

public class Physics
{
	public static final double MAX_SPEED = 1000.0f;
	private List<PhysicsObject> allPhysicsObjects;
    private List<Collider> allColliders;
    private List<PhysicsEffect> globalEffects;
    private QuadTree collidersTree;
	
	private class ColliderPair
	{
		private Collider c1, c2;
		ColliderPair(Collider c, Collider d)
		{
			c1 = c;
			c2 = d;
		}
		@Override
		public boolean equals(Object obj)
		{
			if(!(obj instanceof ColliderPair))
			{
				return false;
			}
			if(obj == null)
			{
				return false;
			}
			ColliderPair other = (ColliderPair) obj;
			return ((other.c1 == c1 && other.c2 == c2) ||
			        (other.c2 == c1 && other.c1 == c2));
		}
		
		@Override
		public int hashCode()
		{
			int hash = c1.hashCode() * c2.hashCode();
			return hash;
		}
	}
	
    public Physics(BoundingBox bounds)
    {
        collidersTree = new QuadTree(bounds);
        allColliders = new ArrayList<>();
        allPhysicsObjects = new ArrayList<>();
        globalEffects = new ArrayList<>();
    }

    public BoundingBox getBounds()
    {
        return collidersTree.getBounds();
    }

    public void update(double elapsedMillis)
    {
        applyEffects(elapsedMillis);
        updateKinematics(elapsedMillis);
        updateBounds();
	handleCollisions();
        removeExpiredObjects();
    }
	
	private void updateKinematics(double elapsedMillis)
	{
		for(PhysicsObject obj : allPhysicsObjects)
		{
			applyVelocity(obj, elapsedMillis);
            applySpeedLimit(obj);
		}
	}

    private void applyEffects(double elapsedMillis)
    {
        for(PhysicsObject obj : allPhysicsObjects)
        {
            for(PhysicsEffect eff : globalEffects)
            {
                eff.apply(obj, elapsedMillis);
            }
        }
    }
	
	private void applyVelocity(PhysicsObject obj, double elapsedMillis)
	{
		long vTranslation = Vectors.scalarMultiply(obj.getVelocity(), elapsedMillis / 1000);
		obj.setPosition(Vectors.add(vTranslation, obj.getPosition()));
	}
	
	private void applySpeedLimit(PhysicsObject obj)
	{
            long vVelocity = obj.getVelocity();
            double speed = Vectors.length(vVelocity);
            if(speed > MAX_SPEED)
            {
                obj.setVelocity(Vectors.scaleToLength(vVelocity, MAX_SPEED));
            }
	}
	
    private void handleCollisions()
    {
        HashSet<ColliderPair> donePairs = new HashSet<>();
        for(Collider coll1 : allColliders)
        {
            List<Collider> possibleCollisions = collidersTree.retrieve(coll1.getBounds());
            for(Collider coll2 : possibleCollisions)
            {
                if(coll1 == coll2)
                {
                    continue;
                }
                ColliderPair thisPair = new ColliderPair(coll1, coll2);
                if(!donePairs.contains(thisPair))
                {
                    Collisions.handle(coll1, coll2);
                    donePairs.add(thisPair);
                }
            }
        }
    }

    public List<Collider> getCollisions(Collider c)
    {
        List<Collider> possibleCollisions = collidersTree.retrieve(c.getBounds());
        List<Collider> collisions = new ArrayList<>();
        for(Collider c2 : possibleCollisions)
        {
            if(Collisions.hasCollided(c2, c))
            {
                collisions.add(c2);
            }
        }
        return collisions;
    }
    
    private void updateBounds()
    {
        for(Collider coll : allColliders)
        {
            if(collidersTree.contains(coll))
            {
                collidersTree.replace(coll, coll);
            }
            else
            {
                collidersTree.insert(coll);
            }
        }
    }

    public List<Collider> getColliders()
    {
        return allColliders;
    }

    public void addCollider(Collider coll)
    {
        allColliders.add(coll);
        allPhysicsObjects.add(coll);
        collidersTree.insert(coll);
    }

    public void addPhysicsObject(PhysicsObject obj)
    {
        allPhysicsObjects.add(obj);
    }
    
    public void removeCollider(Collider coll)
    {
        allColliders.remove(coll);
        allPhysicsObjects.remove(coll);
        collidersTree.remove(coll);
    }

    public void addEffect(PhysicsEffect effect)
    {
        globalEffects.add(effect);
    }

    public void removeEffect(PhysicsEffect effect)
    {
        globalEffects.remove(effect);
    }

    public List<PhysicsObject> getPhysicsObjects()
    {
        return allPhysicsObjects;
    }

    private void removeExpiredObjects()
    {
        ArrayList<PhysicsObject> toDelete = new ArrayList<>();
        for(PhysicsObject obj : allPhysicsObjects)
        {
            if(obj.hasExpired())
            {
                toDelete.add(obj);
            }
        }
        for(PhysicsObject deleting : toDelete)
        {
            allPhysicsObjects.remove(deleting);
            if(allColliders.contains(deleting))
            {
                allColliders.remove((Collider) deleting);
                collidersTree.remove((Collider) deleting);
            }
        }
    }

}
package sketchwars.physics;

import java.util.List;
import java.util.ArrayList;

public class Physics
{
    private List<PhysicsObject> allPhysicsObjects;
    private List<Collider> allColliders;
    private QuadTree collidersTree;

    public Physics(BoundingBox bounds)
    {
        collidersTree = new QuadTree(bounds);
        allColliders = new ArrayList<>();
        allPhysicsObjects = new ArrayList<>();
    }

    public void update(double elapsedMillis)
    {
        //update kinematics
        //detect collisions
        for(Collider coll1 : allColliders)
        {
            List<Collider> possibleCollisions = collidersTree.retrieve(coll1.getBounds());
            for(Collider coll2 : possibleCollisions)
            {
                Collisions.handle(coll1, coll2);
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
        //add to qt.
    }

    public void addPhysicsObject(PhysicsObject obj)
    {
        allPhysicsObjects.add(obj);
    }

    public List<PhysicsObject> getPhysicsObjects()
    {
        return allPhysicsObjects;
    }

}
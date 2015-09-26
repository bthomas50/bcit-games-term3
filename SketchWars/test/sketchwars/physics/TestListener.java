package sketchwars.physics;

import java.util.ArrayList;

public class TestListener implements CollisionListener
{
    public class ColliderPair
    {
        public Collider coll1, coll2;
        public ColliderPair(Collider c1, Collider c2)
        {
            coll1 = c1;
            coll2 = c2;
        }
    }
    
    public ArrayList<ColliderPair> collisions;
    public TestListener()
    {
        collisions = new ArrayList<>();
    }
    @Override
    public void collided(Collider coll1, Collider coll2)
    {
        collisions.add(new ColliderPair(coll1, coll2));
    }
}
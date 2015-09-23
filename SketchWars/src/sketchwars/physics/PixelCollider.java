package sketchwars.physics;

import java.util.List;
import java.util.ArrayList;
/**
 * Class of physics objects that collides based on the intersection of pixel masks
 * @author Brian
 */
public class PixelCollider extends BasicPhysicsObject implements Collider
{
    private BitMask bitMask;
    private List<CollisionListener> listeners;

    public PixelCollider(BitMask bm)
    {
        bitMask = bm;
        listeners = new ArrayList<CollisionListener>();
        bm.trim();
    }

    public BitMask getPixels()
    {
        return bitMask;
    }

    @Override
    public void notify(Collider other)
    {
        for(CollisionListener list : listeners)
        {
            list.collided(this, other);
        }
    }

    @Override
    public BoundingBox getBounds()
    {
        return bitMask.getBounds();
    }

    @Override
    public void addCollisionListener(CollisionListener list)
    {
        listeners.add(list);
    }

    @Override
    public void removeCollisionListener(CollisionListener list)
    {
        listeners.remove(list);
    }
}
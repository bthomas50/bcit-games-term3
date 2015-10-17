package sketchwars.physics;

import sketchwars.game.GameObject;

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
    private float elasticity;

    public PixelCollider(BitMask bm)
    {
        bitMask = bm;
        listeners = new ArrayList<CollisionListener>();
        bm.trim();
		this.vPosition = bm.getPosition();
    }

    public BitMask getPixels()
    {
        return bitMask;
    }

	@Override
	public void setPosition(long vPos)
	{
		super.setPosition(vPos);
		bitMask.setPosition(vPos);
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
    public long getCenterOfMass()
    {
        return bitMask.getCenterOfMass();
    }

    @Override
    public float getElasticity()
    {
        return this.elasticity;
    }
    @Override
    public void setElasticity(float ela)
    {
        this.elasticity = ela;
    }

    @Override
    public boolean isStatic()
    {
        return mass == 0.0;
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

    @Override
    public boolean hasAttachedGameObject()
    {
        return false;
    }

    @Override
    public GameObject getAttachedGameObject()
    {
        return null;
    }
}
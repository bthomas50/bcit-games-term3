package sketchwars.physics;

import sketchwars.game.GameObject;

import java.util.List;
import java.util.ArrayList;
/**
 * Class of physics objects that collides based on the intersection of pixel masks
 * @author Brian
 */
public abstract class AbstractCollider extends BasicPhysicsObject implements Collider
{
    private List<CollisionListener> listeners;
    private float elasticity;
    private GameObject attached;
    private CollisionBehaviour behaviour;

    public AbstractCollider()
    {
        this(CollisionBehaviour.TRANSFER_MOMENTUM);
    }

    public AbstractCollider(CollisionBehaviour behaviour)
    {
        this.behaviour = behaviour;
        listeners = new ArrayList<CollisionListener>();
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
        return (attached != null);
    }

    public void attachGameObject(GameObject obj) {
        this.attached = obj;
    }
    
    @Override
    public GameObject getAttachedGameObject()
    {
        return attached;
    }

    @Override
    public CollisionBehaviour getCollisionBehaviour(Collider o)
    {
        return behaviour;
    }
}
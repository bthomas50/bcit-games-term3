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
    private float staticFriction;
    private float dynamicFriction;
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
        elasticity = 0.0f;
        staticFriction = 0.0f;
        dynamicFriction = 0.0f;
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
    public float getStaticFriction()
    {
        return this.staticFriction;
    }

    @Override
    public void setStaticFriction(float staticFriction)
    {
        this.staticFriction = staticFriction;
    }

    @Override
    public float getDynamicFriction()
    {
        return this.dynamicFriction;
    }

    @Override
    public void setDynamicFriction(float dynamicFriction)
    {
        this.dynamicFriction = dynamicFriction;
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
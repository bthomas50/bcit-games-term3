package sketchwars.physics;

import sketchwars.game.GameObject;

public class GamePixelCollider extends PixelCollider
{
    private GameObject attached;

    public GamePixelCollider(BitMask bm)
    {
        this(null, bm);
    }

    public GamePixelCollider(GameObject obj, BitMask bm)
    {
        super(bm);
        attached = obj;
    }

    public GamePixelCollider(GameObject obj, BitMask bm, CollisionBehaviour beh)
    {
        super(bm, beh);
        attached = obj;
    }

    @Override
    public boolean hasAttachedGameObject()
    {
        return attached != null;
    }

    @Override
    public GameObject getAttachedGameObject()
    {
        return attached;
    }

    @Override
    public boolean hasExpired()
    {
        if(hasAttachedGameObject())
        {
            return attached.hasExpired();
        }
        return false;
    }
}
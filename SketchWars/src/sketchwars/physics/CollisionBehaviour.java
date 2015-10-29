package sketchwars.physics;

public enum CollisionBehaviour
{
    NONE(0), NOTIFY(1), CLIP(2), TRANSFER_MOMENTUM(3);

    //n defines the "priority" of the behaviours. 
    private int n;


    private CollisionBehaviour(int n)
    {
        this.n = n;
    }

    public boolean includes(CollisionBehaviour other)
    {
        return n >= other.n;
    }

    public static CollisionBehaviour min(CollisionBehaviour b1, CollisionBehaviour b2)
    {
        if(b1.n < b2.n)
        {
            return b1;
        }
        else
        {
            return b2;
        }
    }
}
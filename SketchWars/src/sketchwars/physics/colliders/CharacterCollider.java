package sketchwars.physics.colliders;

import sketchwars.game.GameObject;
import sketchwars.physics.*;
/**
 *
 * @author a00861166
 */
public class CharacterCollider extends GamePixelCollider
{
    private static final int MAX_RUN_SPEED = 200;
    private static final float MAX_RUN_ACCELERATION = 4000;
    private static final float JUMP_ACCELERATION = 30000;
    public CharacterCollider(BitMask bm)
    {
        super(bm);
    }
    
    public CharacterCollider(GameObject obj, BitMask bm)
    {
        super(obj, bm);
    }

    public CharacterCollider(GameObject obj, BitMask bm, CollisionBehaviour beh)
    {
        super(obj, bm, beh);
    }
    
    public void moveLeft(double elapsedMillis)
    {
        double velX = Vectors.xComp(vVelocity);
        double desiredVelX = -MAX_RUN_SPEED;
        double deltaX = desiredVelX - velX;
        if(Math.abs(deltaX) > MAX_RUN_ACCELERATION)
        {
            deltaX = MAX_RUN_ACCELERATION * Math.signum(deltaX);
        }
        accelerate(Vectors.create(deltaX, 0), elapsedMillis);
    }
    
    public void moveRight(double elapsedMillis)
    {
        double velX = Vectors.xComp(vVelocity);
        double desiredVelX = MAX_RUN_SPEED;
        double deltaX = desiredVelX - velX;
        if(Math.abs(deltaX) > MAX_RUN_ACCELERATION)
        {
            deltaX = MAX_RUN_ACCELERATION * Math.signum(deltaX);
        }
        accelerate(Vectors.create(deltaX, 0), elapsedMillis);
    }
    
    public void jump(double elapsedMillis)
    {
        accelerate(Vectors.create(0, JUMP_ACCELERATION), elapsedMillis);
    }
}

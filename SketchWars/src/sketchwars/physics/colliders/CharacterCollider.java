package sketchwars.physics.colliders;

import sketchwars.game.GameObject;
import sketchwars.physics.*;
import sketchwars.map.AbstractMap;
import sketchwars.util.Timer;
/**
 *
 * @author a00861166
 */
public class CharacterCollider extends GamePixelCollider implements CollisionListener
{
    private static final int MAX_RUN_SPEED = 200;
    private static final float MAX_RUN_ACCELERATION = 4000;
    private static final float JUMP_ACCELERATION = 500;
    private static final float JUMP_SPEED_THRESHOLD = 200;
    private static final int JUMP_TIME_MILLIS = 500;

    private boolean hasHitGround = false;
    private boolean hasJumpTimeElapsed = false;
    private Timer jumpTimer = new Timer(JUMP_TIME_MILLIS);

    public CharacterCollider(BitMask bm)
    {
        super(bm);
        addCollisionListener(this);
        jumpTimer.start();
    }
    
    public CharacterCollider(GameObject obj, BitMask bm)
    {
        super(obj, bm);
        addCollisionListener(this);
        jumpTimer.start();
    }

    public CharacterCollider(GameObject obj, BitMask bm, CollisionBehaviour beh)
    {
        super(obj, bm, beh);
        addCollisionListener(this);
        jumpTimer.start();
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
    
    public boolean tryJump(double elapsedMillis)
    {
        if(canJump()) 
        {
            accelerate(Vectors.create(0, JUMP_ACCELERATION), 1000);
            resetJumpFlags();
            return true;
        }
        return false;
    }

    public void updateJumpTimer(double elapsedMillis)
    {
        jumpTimer.update(elapsedMillis);
        if(jumpTimer.hasElapsed())
        {
            jumpTimer.restart();
            hasJumpTimeElapsed = true;
        }
    }

    @Override
    public void collided(Collider thisColl, Collider otherColl) 
    {
        if(otherColl.hasAttachedGameObject()) 
        {
            GameObject otherObj = otherColl.getAttachedGameObject();
            if(otherObj instanceof AbstractMap) 
            {
                hasHitGround = true;
            }
        }
    }

    private boolean canJump() 
    {
        return hasHitGround && hasJumpTimeElapsed && Vectors.yComp(vVelocity) < JUMP_SPEED_THRESHOLD;
    }

    private void resetJumpFlags()
    {
        hasHitGround = false;
        hasJumpTimeElapsed = false;
    }
}

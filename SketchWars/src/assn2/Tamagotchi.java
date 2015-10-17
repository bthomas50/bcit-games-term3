package assn2;

import assn2.ai.*;

import sketchwars.graphics.Texture;
import sketchwars.physics.BoundingBox;

import java.util.List;

public class Tamagotchi implements sketchwars.game.GameObject, sketchwars.graphics.GraphicsObject
{
    BoundingBox drawPos;
    FiniteStateMachine<States> stateMachine;
    Texture texture;

    private boolean isFoodAvailable;
    private boolean isPlayAvailable;
    private boolean isStartAvailable;

    private double millisSinceStateChange;
    private double totalAgeInMillis;
    private double millisSinceLastEat;

    public Tamagotchi(BoundingBox bounds, Texture tex)
    {
        drawPos = bounds;
        texture = tex;
        resetCommands();
        millisSinceStateChange = 0.0;
        totalAgeInMillis = 0.0;
        millisSinceLastEat = 0.0;
    }

    public void setStateMachine(FiniteStateMachine<States> fsm)
    {
        stateMachine = fsm;
        fsm.setTransitionListener(new TransitionListener<States>() 
        {
            //this is where we handle state changes
            //the Tamagotchi class could keep a 
            //    Map<States, Action> exitActions and a 
            //    Map<States, Action> entryActions 
            //and execute the appropriate Action from this function, but this is simpler
            @Override
            public void transitioned(States from, States to)
            {
                millisSinceStateChange = 0;
                if(from == States.DEAD)
                {
                    totalAgeInMillis = 0;
                }
                else if(from == States.EATING)
                {
                    millisSinceLastEat = 0;
                }
            }
        });
    }

    //there should usually be at most one command
    public void handleInput(List<ButtonClickCommands> commands)
    {
        //set the flags appropriately
        resetCommands();
        for(ButtonClickCommands command : commands)
        {
            switch(command)
            {
            case FEED:
                isFoodAvailable = true;
                break;
            case PLAY:
                isPlayAvailable = true;
                break;
            case START:
                isStartAvailable = true;
                break;
            }
        }
    }

    public boolean isFoodAvailable()
    {
        return isFoodAvailable;
    }

    public boolean isPlayAvailable()
    {
        return isPlayAvailable;
    }

    public boolean isStartAvailable()
    {
        return isStartAvailable;
    }

    public double getMillisSinceLastStateChange()
    {
        return millisSinceStateChange;
    }

    public double getTotalAgeInMillis()
    {
        return totalAgeInMillis;
    }

    public double getMillisSinceLastEat()
    {
        return millisSinceLastEat;
    }

    @Override
    public void update(double elapsedMillis)
    {
        //check for state transitions
        stateMachine.update();
        //update timers
        millisSinceStateChange += elapsedMillis;
        totalAgeInMillis += elapsedMillis;
        millisSinceLastEat += elapsedMillis;
    }

    @Override
    public void render()
    {
        texture.drawInScreenCoords(drawPos.getLeft(), drawPos.getTop(), drawPos.getWidth(), drawPos.getHeight());
    }

    private void resetCommands()
    {
        isFoodAvailable = false;
        isPlayAvailable = false;
        isStartAvailable = false;
    }
}
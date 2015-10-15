package sketchwars.util;

/**
 * Utility class for Timers that get updated through in the game loop's update() phase
 * @author Brian Thomas <bthomas50@my.bcit.ca>
 */
public class Timer
{
    private double startingMillis;
    private double remainingMillis;
    private State state;

    public Timer(double durationMillis)
    {
        if(durationMillis < 0.0)
        {
            throw new IllegalArgumentException("Duration must be positive!");
        }
        startingMillis = durationMillis;
        remainingMillis = durationMillis;
        state = State.PAUSED;
    }

    public void update(double elapsedMillis)
    {
        if(isRunning())
        {
            remainingMillis -= elapsedMillis;
            if(hasElapsed())
            {
                pause();
                remainingMillis = 0;
            }
        }
    }

    public void start()
    {
        state = State.RUNNING;
    }

    public void pause()
    {
        state = State.PAUSED;
    }

    public void reset()
    {
        state = State.PAUSED;
        remainingMillis = startingMillis;
    }

    public boolean hasElapsed()
    {
        return remainingMillis <= 0.0;
    }

    public boolean isRunning()
    {
        return state == State.RUNNING;
    }

    public double getRemainingMillis() 
    {
        return remainingMillis;
    }

    private enum State
    {RUNNING, PAUSED}
}
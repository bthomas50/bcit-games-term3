package sketchwars.game;

import sketchwars.util.Timer;
/**
 * Class for keeping track of the time that a turn has gone on for.
 * @author Brian Thomas <bthomas50@my.bcit.ca>
 */
public class Turn
{
    private Timer timer;

    Turn(double durationMillis)
    {
        timer = new Timer(durationMillis);
        timer.start();
    }

    public void update(double elapsedMillis)
    {
        timer.update(elapsedMillis);
    }

    public boolean isCompleted()
    {
        return timer.hasElapsed();
    }

    public static Turn createDefaultTurn()
    {
        return new Turn(30000);//30 seconds.
    }
}
package sketchwars.game;

import sketchwars.util.Timer;
import sketchwars.character.SketchCharacter;

import java.util.ArrayList;

/**
 * Class for telling us when the turn is over (either all active characters have fired, or the time expires)
 * @author Brian Thomas <bthomas50@my.bcit.ca>
 */
public class Turn
{
    private ArrayList<SketchCharacter> activeCharacters;
    private Timer timer;

    Turn(double durationMillis)
    {
        activeCharacters = new ArrayList<>();
        timer = new Timer(durationMillis);
        timer.start();
    }

    public void update(double elapsedMillis)
    {
        timer.update(elapsedMillis);
    }

    public void addCharacter(SketchCharacter ch)
    {
        activeCharacters.add(ch);
    }

    public boolean isCompleted()
    {
        return areAllCharactersDone() || timer.hasElapsed();
    }

    private boolean areAllCharactersDone()
    {
        for(SketchCharacter ch : activeCharacters)
        {
            if(!ch.hasFiredThisTurn() && !ch.isDead())
            {
                return false;
            }
        }
        return true;
    }

    public double getRemainingMillis() {
        return timer.getRemainingMillis();
    }

    public static Turn createDefaultTurn()
    {
        return new Turn(30000);//30 seconds.
    }

}
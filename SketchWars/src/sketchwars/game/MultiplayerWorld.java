package sketchwars.game;

import sketchwars.input.*;

import java.util.Map;

/**
 * @author Najash Najimudeen <najash.najm@gmail.com>
 * @author Brian Thomas <bthomas50@my,bcit.ca>
 * @author David Ly <ly_nekros@hotmail.com>
 */
public class MultiplayerWorld extends SketchWarsWorld {

    private final int localIdx;

    public MultiplayerWorld(int localIdx) {
        this.localIdx = localIdx;
    }

    @Override
    protected int getLocalTeamIdx() {
        return localIdx;
    }
}

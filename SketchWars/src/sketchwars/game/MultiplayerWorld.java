package sketchwars.game;

import sketchwars.physics.BoundingBox;

/**
 * @author Najash Najimudeen <najash.najm@gmail.com>
 * @author Brian Thomas <bthomas50@my,bcit.ca>
 * @author David Ly <ly_nekros@hotmail.com>
 */
public class MultiplayerWorld extends SketchWarsWorld {

    private final int localIdx;

    public MultiplayerWorld(int localIdx, int turnTimeSeconds, BoundingBox extendedWorldBoundingBox) {
        super(turnTimeSeconds, extendedWorldBoundingBox);
        this.localIdx = localIdx;
    }

    @Override
    protected int getLocalTeamIdx() {
        return localIdx;
    }
}

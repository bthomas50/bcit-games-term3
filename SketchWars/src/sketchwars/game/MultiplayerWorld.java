package sketchwars.game;

import sketchwars.input.*;

import java.util.Map;

/**
 * @author Najash Najimudeen <najash.najm@gmail.com>
 * @author Brian Thomas <bthomas50@my,bcit.ca>
 * @author David Ly <ly_nekros@hotmail.com>
 */
public class MultiplayerWorld extends SketchWarsWorld {
    private Map<Integer, Input> inputs = null;

    public void update(Map<Integer, Input> inputs, double elapsedMillis) {
        this.inputs = inputs;
        super.update(elapsedMillis);
    }
    
    @Override
    protected void handleInput(double elapsedMillis) {
        for(int i = 0; i < teams.size(); i++) {
            teams.get(i).handleInput(inputs.get(i), elapsedMillis);
        }
    }
}

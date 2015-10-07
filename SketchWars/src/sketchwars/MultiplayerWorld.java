package sketchwars;


import sketchwars.character.Character;
import sketchwars.map.AbstractMap;
import sketchwars.character.Team;
import sketchwars.input.*;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Najash Najimudeen <najash.najm@gmail.com>
 * @author Brian Thomas <bthomas50@my,bcit.ca>
 * @author David Ly <ly_nekros@hotmail.com>
 */
public class MultiplayerWorld extends World {

    public void update(Map<Integer, Input> inputs, double elapsedMillis) {
        handleInput(inputs, elapsedMillis);
        handleCharacterDrowning();
        checkTeamStatus();
        updateObjects(elapsedMillis);
    }
    
    private void handleInput(Map<Integer, Input> inputs, double elapsedMillis) {
        for(int i = 0; i < teams.size(); i++) {
            teams.get(i).handleInput(inputs.get(i), elapsedMillis);
        }
    }
}

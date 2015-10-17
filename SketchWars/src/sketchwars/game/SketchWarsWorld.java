package sketchwars.game;

import sketchwars.character.SketchCharacter;
import sketchwars.map.AbstractMap;
import sketchwars.character.Team;
import sketchwars.input.*;
import sketchwars.game.*;

import java.util.ArrayList;

/**
 * @author Najash Najimudeen <najash.najm@gmail.com>
 * @author Brian Thomas <bthomas50@my,bcit.ca>
 * @author David Ly <ly_nekros@hotmail.com>
 */
public class SketchWarsWorld extends World {    
    protected AbstractMap map;
    protected ArrayList<SketchCharacter> characters;
    protected ArrayList<Team> teams;
    protected Turn currentTurn;
    
    public SketchWarsWorld() {
        characters = new ArrayList<>();
        teams = new ArrayList<>();
        currentTurn = Turn.createDefaultTurn();
    }
    
    public void setMap(AbstractMap map) {
        this.map = map;
        addGameObject(map);
    }

    public void addCharacter(SketchCharacter character) {
        characters.add(character);
        addGameObject(character);
    }
    
    public void addTeam (Team team) {
        teams.add(team);
    }

    @Override
    public void update(double deltaMillis) {
        handleInput(deltaMillis);
        handleCharacterDrowning();
        checkTeamStatus();
        updateObjects(deltaMillis);
        updateTurn(deltaMillis);
        removeExpiredObjects();
    }

    protected void handleInput(double elapsedMillis) {
        for(Team t : teams) {
            t.handleInput(Input.currentInput, elapsedMillis);
        }
    }

    @Override
    public void clear() {
        allObjects.clear();
        characters.clear();
        map = null;
    }
    
    protected void handleCharacterDrowning(){
        for(SketchCharacter character: characters)
        {
            if(character.getPosY() < -5)
            {
                character.setHealth(0);
            }
        }
    }
    
    protected void checkTeamStatus()
    {
        int counter = 0;
        for(Team team: teams)
        {
            if(!team.isDead())
                counter++;
        }
    }

    private void updateTurn(double elapsedMillis) {
        currentTurn.update(elapsedMillis);
        if(currentTurn.isCompleted())
        {
            beginNextTurn();
        }
    }

    private void beginNextTurn() {
        currentTurn = Turn.createDefaultTurn();
        for(Team t : teams) {
            t.cycleActiveCharacter();
            currentTurn.addCharacter(t.getActiveCharacter());
        }
    }

    public ArrayList<SketchCharacter> getCharacters() {
        return characters;
    }
}

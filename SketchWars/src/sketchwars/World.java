package sketchwars;


import sketchwars.character.Character;
import sketchwars.map.AbstractMap;
import sketchwars.character.Team;
import sketchwars.input.*;

import java.util.ArrayList;

/**
 * @author Najash Najimudeen <najash.najm@gmail.com>
 * @author Brian Thomas <bthomas50@my,bcit.ca>
 * @author David Ly <ly_nekros@hotmail.com>
 */
public class World {    
    protected AbstractMap map;
    protected ArrayList<Character> characters;
    protected ArrayList<GameObject> allObjects;
    protected ArrayList<Team> teams;
            
    public World() {
        
        characters = new ArrayList<>();
        teams = new ArrayList<>();
        allObjects = new ArrayList<>();
    }
    
    public void setMap(AbstractMap map) {
        this.map = map;
        allObjects.add(map);
    }

    public void addCharacter(Character character) {
        characters.add(character);
        allObjects.add(character);
    }
    
    public void addTeam (Team team)
    {
        teams.add(team);
    }

    public void addGameObject(GameObject obj) {
        allObjects.add(obj);
    }

    public void update(double deltaMillis) {
        handleInput(deltaMillis);
        handleCharacterDrowning();
        checkTeamStatus();
        updateObjects(deltaMillis);
    }
    
    protected void updateObjects(double deltaMillis) {
        for(GameObject obj : allObjects) {
            obj.update(deltaMillis);
        }
    }

    private void handleInput(double elapsedMillis) {
        for(Team t : teams) {
            t.handleInput(Input.currentInput, elapsedMillis);
        }
    }

    public void clear() {
        allObjects.clear();
        characters.clear();
        map = null;
    }
    
    protected void handleCharacterDrowning(){
        for(Character character: characters)
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
        
        System.out.println("There is " + counter + " teams alive");
    }
}

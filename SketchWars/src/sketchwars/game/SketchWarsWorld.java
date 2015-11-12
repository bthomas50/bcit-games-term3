package sketchwars.game;

import sketchwars.character.SketchCharacter;
import sketchwars.map.AbstractMap;
import sketchwars.character.Team;
import sketchwars.input.*;

import java.util.ArrayList;
import org.lwjgl.glfw.GLFW;
import sketchwars.character.projectiles.AbstractProjectile;
import sketchwars.physics.Collider;
import sketchwars.physics.Vectors;
import sketchwars.scenes.Camera;
import sketchwars.util.Converter;

/**
 * @author Najash Najimudeen <najash.najm@gmail.com>
 * @author Brian Thomas <bthomas50@my,bcit.ca>
 * @author David Ly <ly_nekros@hotmail.com>
 */
public class SketchWarsWorld extends World implements KeyCharListener {    
    protected AbstractMap map;
    protected ArrayList<SketchCharacter> characters;
    protected ArrayList<Team> teams;
    protected Turn currentTurn;
    protected Camera camera;
    
    public SketchWarsWorld() {
        characters = new ArrayList<>();
        teams = new ArrayList<>();
        currentTurn = Turn.createDefaultTurn();
        
        KeyboardHandler.addCharListener((SketchWarsWorld)this);
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
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
        Input.handleGameInput();
        addPendingObjects();
        handleInput(deltaMillis);
        handleCharacterDrowning();
        checkTeamStatus();
        updateObjects(deltaMillis);
        updateTeamBars();
        updateTurn(deltaMillis);
        handlePanningCamera();
        removeExpiredObjects();
    }
    
    protected void handleInput(double elapsedMillis) {
        for(Team t : teams) {
            t.handleInput(Input.currentInput, elapsedMillis);
        }
    }
    
    private void updateTeamBars()
    {
        float counter = 0;
        for (Team t: teams)
        {
            t.updateTotalHealth();
            
            t.getHealthBar().setPosition(camera.getLeft(), camera.getTop() - 1.6f - counter);
            counter += 0.1f;
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

    public AbstractMap getMap() {
        return map;
    }

    private void handlePanningCamera() {
        if (camera != null) {
            Team firstTeam = teams.get(getLocalTeamIdx());
            SketchCharacter character = firstTeam.getActiveCharacter();
            AbstractProjectile projectile = character.getFiredProjectile();
            
            if (camera.isDragResetOn()) {
                if (projectile != null && !projectile.hasExpired()) {
                    Collider coll = projectile.getCollider();
                    long center = coll.getBounds().getCenterVector();
                    float posX = Converter.PhysicsToGraphicsX(Vectors.xComp(center));
                    float posY = Converter.PhysicsToGraphicsY(Vectors.yComp(center));
                    camera.setNextCameraPosition(posX, posY);
                } else {
                    camera.setNextCameraPosition(character.getPosX(), character.getPosY());
                }
            }
        }
    }

    protected int getLocalTeamIdx() {
        return 0;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        KeyboardHandler.removeCharListener(this);
    }
    
    @Override
    public void charTyped(int keycode) {
        if ((char)keycode == 'c') {
            camera.toggleDragReset();
        }
    }
}

package sketchwars.game;

import java.awt.Color;
import sketchwars.character.SketchCharacter;
import sketchwars.map.AbstractMap;
import sketchwars.character.Team;
import sketchwars.input.*;

import java.util.ArrayList;
import java.util.Map;
import org.joml.Vector2d;
import sketchwars.HUD.HealthBar;
import sketchwars.character.projectiles.AbstractProjectile;
import sketchwars.character.projectiles.MineProjectile;
import sketchwars.physics.BoundingBox;
import sketchwars.physics.Collider;
import sketchwars.physics.Vectors;
import sketchwars.scenes.Camera;
import sketchwars.ui.components.Label;
import sketchwars.util.Converter;

/**
 * @author Najash Najimudeen <najash.najm@gmail.com>
 * @author Brian Thomas <bthomas50@my,bcit.ca>
 * @author David Ly <ly_nekros@hotmail.com>
 */
public class SketchWarsWorld extends World implements KeyCharListener {   
    private static final float MAX_CAMERA_DISTANCE = 0.15f;
    
    protected AbstractMap map;
    protected ArrayList<SketchCharacter> characters;
    protected ArrayList<Team> teams;
    protected Turn currentTurn;
    protected Camera camera;
    Label timerLabel;
    int turnTimeSeconds;
    
    private BoundingBox extendedWorldBoundingBox;
    
    public SketchWarsWorld(int turnTimeSeconds, BoundingBox extendedWorldBoundingBox) {
        characters = new ArrayList<>();
        teams = new ArrayList<>();
        currentTurn = new Turn(turnTimeSeconds);
        this.turnTimeSeconds = turnTimeSeconds;
        timerLabel = new Label(String.valueOf(currentTurn.getRemainingMillis()/1000),
                                null,
                                new Vector2d(0,0),
                                new Vector2d(0.65,0.65),
                                null);
        KeyboardHandler.addCharListener((SketchWarsWorld)this);
        this.extendedWorldBoundingBox = extendedWorldBoundingBox;
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
        addPendingObjects();
        handleCharacterDrowning();
        checkTeamStatus();
        updateObjects(deltaMillis);
        updateTeamBars();
        updateTurn(deltaMillis);
        handlePanningCamera();
        updateTimeLabel();
        checkOutOfWorldBoundsObjects();
        removeExpiredObjects();
    }
    
    public void handleInput(Map<Integer, Input> inputs, double elapsedMillis) {
        for(int i = 0; i < teams.size(); i++) {
            Input inputsForTeam = inputs.get(i);
            if(inputsForTeam != null)
            {
                teams.get(i).handleInput(inputs.get(i), elapsedMillis);
            }
        }
    }
    
    private void updateTeamBars()
    {
        int counter = teams.size();
        float cameraBottom = (camera.getTop() - camera.getHeight());
        float cameraLeft = camera.getLeft() ;
        
        for (Team t: teams)
        {
            t.updateTotalHealth();
            
            HealthBar current = t.getHealthBar();
            float x = cameraLeft;
            float y = cameraBottom + (counter * current.getHeight()) + (counter * 0.01f);
            
            t.getHealthBar().setPosition(x, y);
            counter--;
        }
    }
    
    private void updateTimeLabel()
    {

        if (!timerLabel.getText().equals(String.valueOf((int)currentTurn.getRemainingMillis()/1000)))
        {
            timerLabel.setText(String.valueOf((int)currentTurn.getRemainingMillis()/1000));
            if(currentTurn.getRemainingMillis()/1000 < 6)
            {
                timerLabel.setFontColor(Color.RED);
                timerLabel.setSize(new Vector2d(0.9,0.6));
            }
            else
            {
                timerLabel.setFontColor(new Color(0,153,51));
                timerLabel.setSize(new Vector2d(0.55,0.35));
            }
        }
        timerLabel.setPosition(new Vector2d(camera.getLeft() + camera.getWidth()/2, camera.getTop() - 0.1f));
        timerLabel.render();
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
        currentTurn = new Turn(turnTimeSeconds * 1000);//Turn.createDefaultTurn();
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
            
            if (camera.isDragResetOn()) {
                SketchCharacter character = firstTeam.getActiveCharacter();
                AbstractProjectile projectile = character.getFiredProjectile();
                float posX = character.getPosX();
                float posY = character.getPosY();
                
                if (projectile != null) {
                    if (!projectile.hasExpired()) {
                        Collider coll = projectile.getCollider();
                        long center = coll.getBounds().getCenterVector();
                        posX = Converter.PhysicsToGraphicsX(Vectors.xComp(center));
                        posY = Converter.PhysicsToGraphicsY(Vectors.yComp(center));
                    }
                    
                    if (projectile instanceof MineProjectile && projectile.hasStoppedMoving()) {
                        character.clearFiredProjectile();
                    }      
                }

                float distanceX = camera.getCenterX() - posX;
                float distanceY = camera.getCenterY() - posY;
                double distance = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));

                if (distance > MAX_CAMERA_DISTANCE) {
                    camera.setNextCameraPosition(posX, posY);
                } else {
                    camera.setCameraPosition(posX, posY);
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

    private void checkOutOfWorldBoundsObjects() {
        for(GameObject obj : allObjects) {
            if (obj instanceof AbstractProjectile) {
                AbstractProjectile projectile = (AbstractProjectile)obj;
                Collider coll = projectile.getCollider();
                long pos = coll.getPosition();
                if (!extendedWorldBoundingBox.contains(
                        (int)Vectors.xComp(pos), (int)Vectors.yComp(pos))) {
                    projectile.setExpired();
                }
            } else if (obj instanceof SketchCharacter) {
                SketchCharacter character = (SketchCharacter)obj;
                Collider coll = character.getCollider();
                long pos = coll.getPosition();
                if (!extendedWorldBoundingBox.contains(
                        (int)Vectors.xComp(pos), (int)Vectors.yComp(pos))) {
                    character.takeDamage(character.getMaxHealth());
                }
            }
        }
    }
}

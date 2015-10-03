package sketchwars;

import static org.lwjgl.glfw.GLFW.*;

import sketchwars.character.Character;
import sketchwars.map.AbstractMap;
import sketchwars.input.KeyboardHandler;
import sketchwars.sound.SoundPlayer;
import java.util.ArrayList;

/**
 * @author Najash Najimudeen <najash.najm@gmail.com>
 * @author Brian Thomas <bthomas50@my,bcit.ca>
 */
public class World {
        
    private AbstractMap map;
    private ArrayList<Character> characters;
    private ArrayList<GameObject> allObjects;
    private SoundPlayer sound;
    public World() {
        characters = new ArrayList<>();
        allObjects = new ArrayList<>();
        SoundPlayer.loadSound();
    }
    
    public void setMap(AbstractMap map) {
        this.map = map;
        allObjects.add(map);
    }

    public void addCharacter(Character character) {
        characters.add(character);
        allObjects.add(character);
    }

    public void addGameObject(GameObject obj) {
        allObjects.add(obj);
    }

    public void update(double deltaMillis) {
        handleInput();
        updateObjects(deltaMillis);
    }
    
    private void updateObjects(double deltaMillis) {
        for(GameObject obj : allObjects) {
            obj.update(deltaMillis);
        }
    }

    private void handleInput() {

        if(KeyboardHandler.isKeyDown(GLFW_KEY_W)){
            System.out.println("W is pressed");
            try{
                SoundPlayer.playMusic(0, true, 0);
            }
            catch (Exception e)
            {
                System.err.println("Problem playing sound");
            }
        } else if(KeyboardHandler.isKeyDown(GLFW_KEY_S)){
            System.out.println("S is pressed");
            try{
                SoundPlayer.playSFX(1, true, 0);
            }
            catch (Exception e)
            {
                System.err.println("Problem playing sound");
            }
        }
        
        if(KeyboardHandler.isKeyDown(GLFW_KEY_A)){
            System.out.println("A is pressed");
        } else if(KeyboardHandler.isKeyDown(GLFW_KEY_D)){
            System.out.println("D is pressed");
        }
        
        if(KeyboardHandler.isKeyDown(GLFW_KEY_SPACE)){
            System.out.println("Space is pressed");
        }
        
        if(KeyboardHandler.isKeyDown(GLFW_KEY_UP)){
            //increment angle
        } else if(KeyboardHandler.isKeyDown(GLFW_KEY_DOWN)){
            //decrement angle
        }
    }

    public void clear() {
        allObjects.clear();
        characters.clear();
        map = null;
    }
}

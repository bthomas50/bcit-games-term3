/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.scenes;

import java.util.HashMap;
import sketchwars.exceptions.SceneMangerException;

/**
 * Manage scenes like game scene and menu scenes
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class SceneManager {            
    private final HashMap<Integer, AbstractScene> scenes;
    private int currentScene;
    
    public SceneManager() {
        scenes = new HashMap<>();
        currentScene = -1;
    }

    public void init() {
        
    }
    
    public void addScene(int index, AbstractScene scene) throws SceneMangerException {
        if (index < 0) {
            throw new SceneMangerException("Scene index must be a positive integer.");
        } else if (scenes.containsKey(index)) {
            throw new SceneMangerException("A scene with the same index already exists.");            
        } else {
            scenes.put(index, scene);
        }
    }
    
    public void removeScene(int index) throws SceneMangerException {
        if (index < 0) {
            throw new SceneMangerException("Scene index must be a positive integer.");
        } else if (scenes.containsKey(index)) {
            scenes.remove(index);        
        } else {
            throw new SceneMangerException("Given scene index does not exist.");             
        }
    }
    
    public void setCurrentScene(int index) throws SceneMangerException  {
        if (scenes.containsKey(index)) {
            currentScene = index;
        } else {
            throw new SceneMangerException("Given scene index does not exist.");
        }
    }
    
    public void render() {
        if (currentScene != -1) {
            AbstractScene scene = scenes.get(currentScene);
            scene.render();
        }
    }

    public AbstractScene getScene(int index) throws SceneMangerException {
        if (index < 0) {
            throw new SceneMangerException("Scene index must be a positive integer.");
        } else if (scenes.containsKey(index)) {
            return scenes.get(index);        
        } else {
            throw new SceneMangerException("Given scene index does not exist.");             
        }
    }
}

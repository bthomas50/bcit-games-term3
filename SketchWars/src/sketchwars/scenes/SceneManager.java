/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.scenes;

import java.util.HashMap;
import sketchwars.exceptions.SceneManagerException;

/**
 * Manage scenes like game scene and menu scenes
 * @author Najash Najimudeen <najash.najm@gmail.com>
 * @param <T> key type
 */
public class SceneManager <T> {            
    private final HashMap<T, Scene> scenes;
    private Scene currentScene;
    private T currentSceneType;
    
    public SceneManager() {
        scenes = new HashMap<>();
        currentScene = null;
    }
    
    public void addScene(T key, Scene scene) throws SceneManagerException {
        if (scenes.containsKey(key)) {
            throw new SceneManagerException("A scene with the same key already exists.");            
        } else {
            scenes.put(key, scene);
        }
    }
    
    public void removeScene(T key) throws SceneManagerException {
        if (scenes.containsKey(key)) {
            scenes.remove(key);        
        } else {
            throw new SceneManagerException("Given scene key does not exist.");             
        }
    }
    
    public void setCurrentScene(T key) throws SceneManagerException  {
        if (scenes.containsKey(key)) {
            currentScene = scenes.get(key);
            currentSceneType = key;
        } else {
            throw new SceneManagerException("Given scene key does not exist.");
        }
    }
    
    public Scene getCurrentScene() {
        return currentScene;
    }
    
    public T getCurrentSceneType() {
        return currentSceneType;
    }
    
    public void render() {
        if (currentScene != null) {
            currentScene.render();
        }
    }

    public Scene getScene(T key) throws SceneManagerException {
        if (scenes.containsKey(key)) {
            return scenes.get(key);        
        } else {
            throw new SceneManagerException("Given scene key does not exist.");             
        }
    }
    
    public void update(double delta) {
        if (currentScene != null) {
            currentScene.update(delta);
        }
    }
}

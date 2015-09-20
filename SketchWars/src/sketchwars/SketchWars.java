/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars;

import sketchwars.exceptions.SceneMangerException;
import sketchwars.scenes.GameScene;
import sketchwars.scenes.SceneManager;


/**
 * The SketchWars main class
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class SketchWars {
    public enum Scenes {
        GAME, MAIN_MENU;
    }
    
    private OpenGL openGL;
    private World world;
    
    private SceneManager sceneManager;
    private GameScene gameScene;
    
    private void init() {
        initScenes();
       
        openGL = new OpenGL(this, sceneManager);
        openGL.init();

        world = new World(sceneManager);
        world.init();
    }
    
    private void initScenes() {
        sceneManager = new SceneManager();
        sceneManager.init();
       
        gameScene = new GameScene();
        gameScene.init();
       
        try {   
            sceneManager.addScene(SketchWars.Scenes.GAME.ordinal(), gameScene);
            sceneManager.setCurrentScene(SketchWars.Scenes.GAME.ordinal());
        } catch (SceneMangerException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    private void start() {
        openGL.run();
    }
    
    public void update(double elapsed) {
        world.update(elapsed);
    }
    
    public static void main(String[] args) {
        SketchWars sketchWars = new SketchWars();        
        sketchWars.init();
        sketchWars.start();
    }

    public void dispose() {
        world.dispose();
    }
}

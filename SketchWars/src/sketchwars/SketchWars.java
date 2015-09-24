/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars;

import sketchwars.exceptions.SceneManagerException;
import sketchwars.graphics.Texture;
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
    
    private SceneManager<Scenes> sceneManager;
    private GameScene gameScene;
    
    private void init() {
        initScenes();
       
        openGL = new OpenGL(this, sceneManager);
        openGL.init();

        world = new World(sceneManager);
        world.init();
    }
    
    private void initScenes() {
        sceneManager = new SceneManager<>();
        sceneManager.init();
       
        gameScene = new GameScene();
        gameScene.init();
       
        try {   
            sceneManager.addScene(SketchWars.Scenes.GAME, gameScene);
            sceneManager.setCurrentScene(SketchWars.Scenes.GAME);
        } catch (SceneManagerException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    private void start() {
        openGL.run();
    }
    
    /**
     * 
     * @param delta frame length in millisecond
     */
    public void update(double delta) {
        world.update(delta);
    }
    
    public static void main(String[] args) {
        SketchWars sketchWars = new SketchWars();        
        sketchWars.init();
        sketchWars.start();
    }

    public void dispose() {
        world.dispose();
        Texture.disposeAllTextures(); //not sure where to delete all the textures from the texure bank
    }
}

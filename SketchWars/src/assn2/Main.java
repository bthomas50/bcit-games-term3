package assn2;

import sketchwars.exceptions.SceneManagerException;
import sketchwars.graphics.Texture;
import sketchwars.physics.*;
import sketchwars.scenes.*;
import sketchwars.input.*;
import sketchwars.game.*;
import sketchwars.sound.SoundPlayer;


import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.Sys;

public class Main
{
    private static final double MILLION = 1000000;//used in calculating frame length
        
    private OpenGL openGL;
    private World world;
    
    private SceneManager<Scenes> sceneManager;
    private double lastTime;
    
    public static void main(String[] args) {
        Main game = new Main();
        game.init();
        game.start();
    }

    private void init() {
        sceneManager = new SceneManager<>();
        
        openGL = new OpenGL();
        openGL.init();

        Scene gameScene = new Scene();
        try {
            sceneManager.addScene(Scenes.GAME, gameScene);
            sceneManager.setCurrentScene(Scenes.GAME);
        } catch (SceneManagerException ex) {
            Logger.getLogger(SketchWars.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        world = new World();
    }
    
    
    public void start() {
        lastTime = System.nanoTime();
 
        try {
            // Run the rendering loop until the user has attempted to close
            // the window or has pressed the ESCAPE key.
            while (!openGL.windowsIsClosing()) {
                Input.update();
                openGL.beginUpdate();
                double time = System.nanoTime(); //calculate frame length in milliseconds
                double delta = (time - lastTime) / MILLION;

                sceneManager.render();
                sceneManager.update(delta);
                
                world.update(delta);

                lastTime = time;

                openGL.endUpdate();
            }
        } finally {
            openGL.dispose();
        }
    }
    
    public void dispose() {
        world.clear();
        Texture.disposeAllTextures(); //not sure where to delete all the textures from the texure bank
    }
}

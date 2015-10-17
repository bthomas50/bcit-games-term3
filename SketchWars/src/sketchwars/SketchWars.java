package sketchwars;

import sketchwars.exceptions.SceneManagerException;
import sketchwars.graphics.Texture;
import sketchwars.physics.*;
import sketchwars.scenes.*;
import sketchwars.input.*;
import sketchwars.game.*;
import sketchwars.sound.SoundPlayer;
import static sketchwars.util.Config.*;


import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.Sys;
/**
 * The SketchWars main class
 * @author Najash Najimudeen <najash.najm@gmail.com>
 * @author Brian Thomas <bthomas50@my.bcit.ca>
 */
public class SketchWars {
    private static final double MILLION = 1000000;//used in calculating frame length
        
    private OpenGL openGL;
    private SketchWarsWorld world;
    private Physics physics;
    
    private SceneManager<Scenes> sceneManager;
    private double lastTime;
    
    public static void main(String[] args) {
        appendToLibraryPath("lib/native/");
        SketchWars sketchWars = new SketchWars();
        sketchWars.init();
        sketchWars.start();
    }

    private void init() {
        sceneManager = new SceneManager<>();
        
        openGL = new OpenGL();
        openGL.init();

        SoundPlayer.loadSound();

        Scene gameScene = new Scene();
        try {
            sceneManager.addScene(Scenes.GAME, gameScene);
            sceneManager.setCurrentScene(Scenes.GAME);
        } catch (SceneManagerException ex) {
            Logger.getLogger(SketchWars.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        physics = new Physics(new BoundingBox(-1024, -1024, 1024, 1024));
        world = new SketchWarsWorld();

        SketchWarsWorldFactory fact = new SketchWarsWorldFactory(world, physics, sceneManager);
        fact.startGame();
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

                if (sceneManager != null) {
                    sceneManager.render();//call the main graphics renderer
                    sceneManager.update(delta);
                }
                
                world.update(delta);
                physics.update(delta);

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

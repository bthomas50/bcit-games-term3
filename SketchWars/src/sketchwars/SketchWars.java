package sketchwars;

import java.util.logging.Level;
import java.util.logging.Logger;
import sketchwars.exceptions.SceneManagerException;
import sketchwars.game.SketchWarsWorld;
import sketchwars.game.SketchWarsWorldFactory;
import sketchwars.graphics.Texture;
import sketchwars.input.Input;
import sketchwars.physics.BoundingBox;
import sketchwars.physics.Physics;
import sketchwars.scenes.Scene;
import sketchwars.scenes.SceneManager;
import sketchwars.sound.SoundPlayer;
import sketchwars.ui.menu.MainMenu;
import sketchwars.ui.menu.OptionMenu;
import static sketchwars.util.Config.appendToLibraryPath;

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
        openGL.init(false);

        SoundPlayer.loadSound();

        Scene gameScene = new Scene();
        MainMenu mainMenuScene = new MainMenu(sceneManager);
        OptionMenu optionMenuScene = new OptionMenu(sceneManager);
        
        try {
            sceneManager.addScene(Scenes.GAME, gameScene);
            sceneManager.addScene(Scenes.MAIN_MENU, mainMenuScene);
            sceneManager.addScene(Scenes.SUB_MENU, optionMenuScene);
            
            sceneManager.setCurrentScene(Scenes.MAIN_MENU);
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

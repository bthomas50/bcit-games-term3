package sketchwars;

import sketchwars.exceptions.SceneManagerException;
import sketchwars.graphics.Texture;
import sketchwars.physics.*;
import sketchwars.scenes.*;
import sketchwars.sound.SoundPlayer;
import sketchwars.input.*;
import sketchwars.game.*;
import network.Peer;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;
import org.lwjgl.Sys;
import static sketchwars.SketchWars.PHYSICS_HEIGHT;
import static sketchwars.SketchWars.PHYSICS_LEFT;
import static sketchwars.SketchWars.PHYSICS_TOP;
import static sketchwars.SketchWars.PHYSICS_WIDTH;

/**
 * The SketchWars main class
 * @author Najash Najimudeen <najash.najm@gmail.com>
 * @author Brian Thomas <bthomas50@my.bcit.ca>
 */
public class MultiplayerSketchWars {
    //used in calculating frame length
    private static final double NANOS_PER_MILLI = 1000000;
    
    private OpenGL openGL;
    private MultiplayerWorld world;
    private Physics physics;
    private Peer network;
    
    private SceneManager<Scenes> sceneManager;
    private double lastTime;
    
    public MultiplayerSketchWars(Peer networkInterface) {
        network = networkInterface;
        init();
    }

    private void init() {
        sceneManager = new SceneManager<>();
        
        openGL = new OpenGL();
        openGL.init(false);

        SoundPlayer.loadSound();

        Scene gameScene = new Scene();
        try {
            sceneManager.addScene(Scenes.GAME, gameScene);
            sceneManager.setCurrentScene(Scenes.GAME);
        } catch (SceneManagerException ex) {
            Logger.getLogger(MultiplayerSketchWars.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        physics = new Physics(new BoundingBox(PHYSICS_TOP, PHYSICS_LEFT, 
                PHYSICS_TOP + PHYSICS_WIDTH, PHYSICS_LEFT + PHYSICS_WIDTH));
        world = new MultiplayerWorld();

        SketchWarsWorldFactory fact = new SketchWarsWorldFactory(world, physics, sceneManager);
        fact.startGame();
    }
    
    
    public void start() {
        lastTime = System.nanoTime();
        int frameNum = 0;
        try {
            // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
            while (!openGL.windowsIsClosing()) {
                Input.handleGameInput();
                //do network stuff.
                network.broadcastInput(frameNum);
                Map<Integer, Input> allInputs = network.getInputs(frameNum);

                openGL.beginUpdate();
                double time = System.nanoTime(); //calculate frame length in milliseconds
                double delta = 16;//(time - lastTime) / NANOS_PER_MILLI;

                if (sceneManager != null) {
                    sceneManager.render();//call the main graphics renderer
                    sceneManager.update(delta);
                }
                
                world.update(allInputs, delta);
                physics.update(delta);

                lastTime = time;

                openGL.endUpdate();
                
                frameNum++;
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

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
import java.util.Random;

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
    
    public MultiplayerSketchWars(Peer networkInterface, Random rng) {
        network = networkInterface;
        init(rng);
    }

    private void init(Random rng) {
        sceneManager = new SceneManager<>();
        
        openGL = new OpenGL();
        openGL.init(false);

        SoundPlayer.loadSound();

        Camera gameCamera = new Camera(SketchWars.OPENGL_LEFT, SketchWars.OPENGL_TOP, 
                                       SketchWars.OPENGL_WIDTH, SketchWars.OPENGL_HEIGHT);
        Scene gameScene = new Scene(gameCamera);
        try {
            sceneManager.addScene(Scenes.GAME, gameScene);
            sceneManager.setCurrentScene(Scenes.GAME);
        } catch (SceneManagerException ex) {
            Logger.getLogger(MultiplayerSketchWars.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        physics = new Physics(new BoundingBox(PHYSICS_TOP, PHYSICS_LEFT, 
                PHYSICS_TOP + PHYSICS_HEIGHT, PHYSICS_LEFT + PHYSICS_WIDTH));
        world = new MultiplayerWorld(network.getLocalId());

        new SketchWarsWorldFactory(world, physics, sceneManager, rng).startGame();
    }
    
    
    public void start() {
        int frameNum = 0;
        try {
            while (!openGL.windowsIsClosing()) {
                Input.update();
                Input.handleGameInput();
                //do network stuff.
                network.broadcastInput(frameNum);
                Map<Integer, Input> allInputs = network.getInputs(frameNum);

                openGL.beginUpdate();
                double delta = 16;

                if (sceneManager != null) {
                    sceneManager.render();
                    sceneManager.update(delta);
                }
                
                world.update(allInputs, delta);
                physics.update(delta);

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

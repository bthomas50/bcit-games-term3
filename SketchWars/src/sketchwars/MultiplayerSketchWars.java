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
import network.GameSetting;

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
    
    private OpenGL openGL;
    private MultiplayerWorld world;
    private Physics physics;
    private final Peer network;
    private SceneManager<Scenes> sceneManager;
    private InputSource inputter;
    
    public MultiplayerSketchWars(Peer networkInterface, Random rng, GameSetting setting) {
        network = networkInterface;
        init(rng, setting);
    }

    private void init(Random rng, GameSetting setting) {
        sceneManager = new SceneManager<>();
        inputter = new MultiInputSource(network);
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

        new SketchWarsWorldFactory(world, physics, sceneManager, rng).startGame(setting);
    }
    
    
    public void start() {
        try {
            while (!openGL.windowsIsClosing()) {
                
                Map<Integer, Input> inputs = inputter.getCurrentInputs();

                openGL.beginUpdate();
                double delta = 16;

                if (sceneManager != null) {
                    sceneManager.render();
                    sceneManager.update(delta);
                }
                world.handleInput(inputs, delta);
                world.update(delta);
                physics.update(delta);

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

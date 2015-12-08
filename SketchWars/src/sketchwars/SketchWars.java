package sketchwars;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;
import network.GameSetting;
import network.Peer;
import sketchwars.exceptions.SceneManagerException;
import sketchwars.game.MultiplayerWorld;
import sketchwars.game.SketchWarsWorld;
import sketchwars.game.SketchWarsWorldFactory;
import sketchwars.graphics.Texture;
import sketchwars.input.*;
import sketchwars.physics.*;
import sketchwars.scenes.*;
import sketchwars.sound.SoundPlayer;
import sketchwars.ui.menu.*;
import static sketchwars.util.Config.appendToLibraryPath;

/**
 * The SketchWars main class
 * @author Najash Najimudeen <najash.najm@gmail.com>
 * @author Brian Thomas <bthomas50@my.bcit.ca>
 */
public class SketchWars {
    public static final int WORLD_SCALE = 2;
    
    public static final int PHYSICS_TOP = -1024 * WORLD_SCALE;
    public static final int PHYSICS_LEFT = -1024 * WORLD_SCALE;
    public static final int PHYSICS_WIDTH = 2048 * WORLD_SCALE;
    public static final int PHYSICS_HEIGHT = 2048 * WORLD_SCALE;
    
    public static final float OPENGL_TOP = 1 * WORLD_SCALE;
    public static final float OPENGL_LEFT = -1 * WORLD_SCALE;
    public static final float OPENGL_WIDTH = 2 * WORLD_SCALE;
    public static final float OPENGL_HEIGHT = 2 * WORLD_SCALE;
    
    private static final double MILLION = 1000000;//used in calculating frame length
    private static final double MAX_FRAME_DELTA = 33;
    private OpenGL openGL;
    private SketchWarsWorld world;
    private Physics physics;
    private InputSource inputter;
    private SceneManager<Scenes> sceneManager;
    private double lastTime;
        
    public static void main(String[] args) {
        appendToLibraryPath("lib/native/");
        SketchWars sketchWars = new SketchWars();
        sketchWars.init();
        sketchWars.loop();
    }
    
    private void init() {
        inputter = new SingleInputSource();
        initOpenGL();
        initScenes();
        SoundPlayer.loadSound();
    }

    private void initOpenGL() {
        openGL = new OpenGL();
        openGL.init(false);
    }
    
    private void initScenes() {
        sceneManager = new SceneManager<>();
        
        Camera menuCamera = new Camera(-1, 1, 2, 2);
        Camera gameCamera = new Camera(SketchWars.OPENGL_LEFT, SketchWars.OPENGL_TOP, 
                                       SketchWars.OPENGL_WIDTH, SketchWars.OPENGL_HEIGHT);

        Scene gameScene = new Scene(gameCamera);
        
        LobbyMenu lobbyMenuScene = new LobbyMenu(sceneManager, this, menuCamera);
        MainMenu mainMenuScene = new MainMenu(sceneManager, this, lobbyMenuScene, menuCamera);
        OptionMenu optionMenuScene = new OptionMenu(sceneManager, menuCamera);
        GameSettingMenu gameSettingMenuScene = new GameSettingMenu(sceneManager, lobbyMenuScene, menuCamera);
        
        try {
            sceneManager.addScene(Scenes.GAME, gameScene);
            sceneManager.addScene(Scenes.MAIN_MENU, mainMenuScene);
            sceneManager.addScene(Scenes.SUB_MENU, optionMenuScene);
            sceneManager.addScene(Scenes.CREATE_MENU, lobbyMenuScene);
            sceneManager.addScene(Scenes.GAME_SETTING_MENU, gameSettingMenuScene);
            
            sceneManager.setCurrentScene(Scenes.MAIN_MENU);
            
        } catch (SceneManagerException ex) {
            Logger.getLogger(SketchWars.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void startTutorial() {
        inputter = new SingleInputSource();
        physics = new Physics(new BoundingBox(PHYSICS_TOP, PHYSICS_LEFT, 
                PHYSICS_TOP + PHYSICS_HEIGHT, PHYSICS_LEFT + PHYSICS_WIDTH));
        world = new SketchWarsWorld();
        new SketchWarsWorldFactory(world, physics, sceneManager, new Random()).startGame(GameSetting.createTutorialSettings());
        
        if (sceneManager != null) {
            try {
                sceneManager.setCurrentScene(Scenes.GAME);
            } catch (SceneManagerException ex) {
                Logger.getLogger(SketchWars.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void startMultiplayer(Peer network, Random rng, GameSetting setting) {
        inputter = new MultiInputSource(network);
        physics = new Physics(new BoundingBox(PHYSICS_TOP, PHYSICS_LEFT, 
                PHYSICS_TOP + PHYSICS_HEIGHT, PHYSICS_LEFT + PHYSICS_WIDTH));
        world = new MultiplayerWorld(network.getLocalId());
        new SketchWarsWorldFactory(world, physics, sceneManager, rng).startGame(setting);
        
        if (sceneManager != null) {
            try {
                sceneManager.setCurrentScene(Scenes.GAME);
            } catch (SceneManagerException ex) {
                Logger.getLogger(SketchWars.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void loop() {
        try {
            // Run the rendering loop until the user has attempted to close
            // the window or has pressed the ESCAPE key.
            while (!openGL.windowsIsClosing()) {
                Map<Integer, Input> inputs = inputter.getCurrentInputs();
                openGL.beginUpdate();
                double delta = 16;
                Scenes current = sceneManager.getCurrentSceneType();
                if (sceneManager != null && current != null) {
                    sceneManager.render();//call the main graphics renderer
                    sceneManager.update(delta);
                    
                    if (current == Scenes.GAME) {
                        world.handleInput(inputs, delta);
                        world.update(delta);
                        physics.update(delta);
                    }
                }
                
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
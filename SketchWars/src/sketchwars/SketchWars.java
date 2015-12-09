package sketchwars;

import java.io.IOException;
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
    public static final float EXTENDED_BB_RANGE = 1.5f;
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
        boolean fullscreen = false;
        
        if (args.length == 1) {
            fullscreen = (args[0].equals("-f"));
        }
        
        appendToLibraryPath("lib/native/");
        SketchWars sketchWars = new SketchWars();
        sketchWars.init(fullscreen);
        sketchWars.loop();
    }
    
    private void init(boolean fullscreen) {
        inputter = new SingleInputSource();
        initOpenGL(fullscreen);
        initScenes();
        SoundPlayer.loadSound();
    }

    private void initOpenGL(boolean fullscreen) {
        openGL = new OpenGL();
        openGL.init(fullscreen);
    }
    
    private void initScenes() {
        sceneManager = new SceneManager<>();
        
        Camera menuCamera = new Camera(-1, 1, 2, 2);
        
        LobbyMenu lobbyMenuScene = new LobbyMenu(sceneManager, this, menuCamera);
        FindHostMenu findHostMeueScene = new FindHostMenu(sceneManager,lobbyMenuScene, menuCamera);
        MainMenu mainMenuScene = new MainMenu(sceneManager, this, lobbyMenuScene, findHostMeueScene, menuCamera);
        OptionMenu optionMenuScene = new OptionMenu(sceneManager, menuCamera);
        TutorialSequence tutorial = new TutorialSequence(sceneManager, menuCamera);
        GameSettingMenu gameSettingMenuScene = new GameSettingMenu(sceneManager, lobbyMenuScene, menuCamera);
        
        try {
            sceneManager.addScene(Scenes.MAIN_MENU, mainMenuScene);
            sceneManager.addScene(Scenes.SUB_MENU, optionMenuScene);
            sceneManager.addScene(Scenes.LOBBY_MENU, lobbyMenuScene);
            sceneManager.addScene(Scenes.FINDHOST_MENU, findHostMeueScene);
            sceneManager.addScene(Scenes.GAME_SETTING_MENU, gameSettingMenuScene);
            sceneManager.addScene(Scenes.TUTORIAL_SEQUENCE, tutorial);
            sceneManager.setCurrentScene(Scenes.MAIN_MENU);
            
        } catch (SceneManagerException ex) {
            Logger.getLogger(SketchWars.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void createGameScene() throws SceneManagerException
    {
        Camera gameCamera = new Camera(SketchWars.OPENGL_LEFT, SketchWars.OPENGL_TOP, 
                                       SketchWars.OPENGL_WIDTH, SketchWars.OPENGL_HEIGHT);
        Scene gameScene = new Scene(gameCamera);
        sceneManager.setScene(Scenes.GAME, gameScene);
    }
    public void startTutorial() {
        inputter = new SingleInputSource();

        BoundingBox physicsBB = new BoundingBox(PHYSICS_TOP, PHYSICS_LEFT, 
                PHYSICS_TOP + PHYSICS_HEIGHT, PHYSICS_LEFT + PHYSICS_WIDTH);
        physics = new Physics(physicsBB);
        
        BoundingBox extendedWorldBoundingBox = new BoundingBox((int)(physicsBB.getTop() * EXTENDED_BB_RANGE), 
                                                   (int)(physicsBB.getLeft() * EXTENDED_BB_RANGE), 
                                                   (int)(physicsBB.getBottom() * EXTENDED_BB_RANGE) ,
                                                   (int)(physicsBB.getRight()* EXTENDED_BB_RANGE));
        
        GameSetting tutorialSettings = GameSetting.createTutorialSettings();
        world = new SketchWarsWorld(tutorialSettings.getTimePerTurn(), extendedWorldBoundingBox);
        try {
            createGameScene();
            new SketchWarsWorldFactory(world, physics, sceneManager, new Random()).startGame(tutorialSettings);
            sceneManager.setCurrentScene(Scenes.GAME);
        } catch (SceneManagerException ex) {
            Logger.getLogger(SketchWars.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void startMultiplayer(Peer network, Random rng, GameSetting setting) {
        inputter = new MultiInputSource(network);
        BoundingBox physicsBB = new BoundingBox(PHYSICS_TOP, PHYSICS_LEFT, 
                PHYSICS_TOP + PHYSICS_HEIGHT, PHYSICS_LEFT + PHYSICS_WIDTH);
        physics = new Physics(physicsBB);
        BoundingBox extendedWorldBoundingBox = new BoundingBox((int)(physicsBB.getTop() * EXTENDED_BB_RANGE), 
                                                   (int)(physicsBB.getLeft() * EXTENDED_BB_RANGE), 
                                                   (int)(physicsBB.getBottom() * EXTENDED_BB_RANGE),
                                                   (int)(physicsBB.getRight() * EXTENDED_BB_RANGE));
        
        world = new MultiplayerWorld(network.getLocalId(), setting.getTimePerTurn(), extendedWorldBoundingBox);
        try {
            createGameScene();
            new SketchWarsWorldFactory(world, physics, sceneManager, rng).startGame(setting);
            sceneManager.setCurrentScene(Scenes.GAME);
        } catch (SceneManagerException ex) {
            Logger.getLogger(SketchWars.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loop() {
        try {
            // Run the rendering loop until the user has attempted to close
            // the window or has pressed the ESCAPE key.
            while (!openGL.windowsIsClosing()) {
                Map<Integer, Input> inputs;
                try {
                    inputs = inputter.getCurrentInputs();
                } catch(IOException e) {
                    inputter = new SingleInputSource();
                    inputs = ((SingleInputSource)inputter).getCurrentInputs();
                    try {
                        sceneManager.setCurrentScene(Scenes.MAIN_MENU);
                    } catch (SceneManagerException ex) {
                        Logger.getLogger(SketchWars.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
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
                        if(world.isGameOver()) {
                            try {
                                sceneManager.setCurrentScene(Scenes.MAIN_MENU);
                            } catch (SceneManagerException ex) {
                                Logger.getLogger(SketchWars.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
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
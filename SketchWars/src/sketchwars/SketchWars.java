package sketchwars;

import sketchwars.exceptions.SceneManagerException;
import sketchwars.graphics.Texture;
import sketchwars.physics.Physics;
import sketchwars.physics.BoundingBox;
import sketchwars.scenes.GameScene;
import sketchwars.scenes.SceneManager;


/**
 * The SketchWars main class
 * @author Najash Najimudeen <najash.najm@gmail.com>
 * @author Brian Thomas <bthomas50@my.bcit.ca>
 */
public class SketchWars {
    public enum Scenes {
        GAME, MAIN_MENU;
    }
    
    private OpenGL openGL;
    private World world;
    private Physics physics;
    
    private SceneManager<Scenes> sceneManager;
    private GameScene gameScene;
    
    public static void main(String[] args) {
        SketchWars sketchWars = new SketchWars();
        sketchWars.init();
        sketchWars.start();
    }

    private void init() {
        initScenes();
       
        openGL = new OpenGL(this, sceneManager);
        openGL.init();

        world = new World();
        physics = new Physics(new BoundingBox(-1024, -1024, 1024, 1024));

        WorldFactory fact = new WorldFactory(world, physics, sceneManager);
        fact.startGame();
    }
    
    private void initScenes() {
        sceneManager = new SceneManager<>();
        sceneManager.init();
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
        physics.update(delta);
    }
    


    public void dispose() {
        world.clear();
        Texture.disposeAllTextures(); //not sure where to delete all the textures from the texure bank
    }
}

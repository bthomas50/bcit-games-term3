package assn2;

import assn2.ai.*;

import sketchwars.exceptions.SceneManagerException;
import sketchwars.graphics.Texture;
import sketchwars.physics.*;
import sketchwars.scenes.*;
import sketchwars.input.*;
import sketchwars.*;
import sketchwars.util.Config;


import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.Sys;

public class Main
{
    private static final double MILLION = 1000000;//used in calculating frame length
        
    private OpenGL openGL;
    private UI ui;
    private Tamagotchi tamagotchi;
    private SceneManager<Scenes> sceneManager;
    private double lastTime;
    
    public static void main(String[] args) {
        Config.appendToLibraryPath("lib/native/");
        Main game = new Main();
        game.init();
        game.start();
    }

    private void init() {
        sceneManager = new SceneManager<>();
        
        openGL = new OpenGL();
        openGL.init();

        Scene<Integer> gameScene = new Scene<>();
        Layer mainLayer = new Layer();
        gameScene.addLayer(0, mainLayer);
        try {
            sceneManager.addScene(Scenes.GAME, gameScene);
            sceneManager.setCurrentScene(Scenes.GAME);

        } catch (SceneManagerException ex) {
            Logger.getLogger(SketchWars.class.getName()).log(Level.SEVERE, null, ex);
        }
        tamagotchi = new Tamagotchi(new BoundingBox(50, 325, 400, 575), Texture.loadTexture("content/char/pikachu.png"));
        FiniteStateMachine<States> stateMachine = TamagotchiStateMachine.create(tamagotchi);
        tamagotchi.setStateMachine(stateMachine);
        ui = TamagotchiUI.create(stateMachine);
        mainLayer.addDrawableObject(ui);
        mainLayer.addDrawableObject(tamagotchi);
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
                
                tamagotchi.handleInput(ui.getCurrentCommands());
                tamagotchi.update(delta);

                lastTime = time;

                openGL.endUpdate();
            }
        } finally {
            openGL.dispose();
        }
    }
    
    public void dispose() {
        Texture.disposeAllTextures(); //not sure where to delete all the textures from the texure bank
    }

}

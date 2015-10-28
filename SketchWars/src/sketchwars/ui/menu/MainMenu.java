/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.ui.menu;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Vector2d;
import sketchwars.OpenGL;
import sketchwars.Scenes;
import sketchwars.SketchWars;
import sketchwars.exceptions.SceneException;
import sketchwars.exceptions.SceneManagerException;
import sketchwars.graphics.GraphicElement;
import sketchwars.graphics.Texture;
import sketchwars.input.MouseHandler;
import sketchwars.input.MouseState;
import sketchwars.scenes.Layer;
import sketchwars.scenes.Scene;
import sketchwars.scenes.SceneManager;
import sketchwars.ui.Button;
import sketchwars.ui.UIActionListener;
import sketchwars.ui.UIComponent;

/**
 *
 * @author A00807688
 */
public class MainMenu extends Scene implements UIActionListener {
    private final SceneManager<Scenes> sceneManager;
    private final SketchWars sketchWars;
    
    private Texture playBtn;
    private Texture playBtnPress;
    private Texture createBtn;
    private Texture createBtnPress;
    private Texture optionsBtn;
    private Texture optionsBtnPress;
    private Texture exitBtn;
    private Texture exitBtnPress;
    private Texture backgroundImage;
      
    private Button buttonPlay;
    private Button buttonCreate;
    private Button buttonOptions;
    private Button buttonExit;
    
    public MainMenu(SceneManager<Scenes> sceneManager, SketchWars sketchWars) {
        this.sceneManager = sceneManager;
        this.sketchWars = sketchWars;
        
        createLayers();
        createButtons();
        createBackground();
    }
    
    private void createLayers()
    {
        Layer bglayer = new Layer();
        bglayer.setZOrder(-1);
        
        Layer btnLayer = new Layer();
        btnLayer.setZOrder(0);
        
        addLayer(MenuLayers.BACKGROUND, bglayer);
        addLayer(MenuLayers.BUTTONS, btnLayer); 
    }

    private void createButtons() {
        //Loading textures
        playBtn = Texture.loadTexture("content/menu/play.png", true);
        playBtnPress = Texture.loadTexture("content/menu/play_press.png", true);
        createBtn = Texture.loadTexture("content/menu/create.png", true);
        createBtnPress = Texture.loadTexture("content/menu/create_press.png", true);
        optionsBtn = Texture.loadTexture("content/menu/options.png", true);
        optionsBtnPress = Texture.loadTexture("content/menu/options_press.png", true);
        exitBtn = Texture.loadTexture("content/menu/exit.png", true);
        exitBtnPress = Texture.loadTexture("content/menu/exit_press.png", true);
        
        Vector2d size = new Vector2d(0.3f,0.12f);

        try {
            Layer btnLayer = getLayer(MenuLayers.BUTTONS);
            
            //play 
            buttonPlay = new Button(new Vector2d(0.03, -0.30), size, playBtn, playBtnPress, null);
            btnLayer.addDrawableObject(buttonPlay);
            buttonPlay.addActionListener(this);
                    
            //create
            buttonCreate = new Button(new Vector2d(0.03, -0.45), size, createBtn, createBtnPress, null);
            btnLayer.addDrawableObject(buttonCreate);            
            buttonCreate.addActionListener(this);
            
            //create
            buttonOptions = new Button(new Vector2d(0.03, -0.60), size, optionsBtn, optionsBtnPress, null);
            btnLayer.addDrawableObject(buttonOptions);     
            buttonOptions.addActionListener(this);
            
            //create
            buttonExit = new Button(new Vector2d(0.03, -0.75), size, exitBtn, exitBtnPress, null);
            btnLayer.addDrawableObject(buttonExit);
            buttonExit.addActionListener(this);
        } catch (SceneException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void createBackground() {
        
        backgroundImage = Texture.loadTexture("content/menu/sketchWars_bg.jpg", false);
        Vector2d size = new Vector2d(2,2);
        try {
            Layer bgLayer = getLayer(MenuLayers.BACKGROUND);
            GraphicElement bg = new GraphicElement(new Vector2d(0,0),size,backgroundImage);
            bgLayer.addDrawableObject(bg);
            
            
        } catch (SceneException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void action(UIComponent component) {
        if (component.equals(buttonCreate)) {
            try {
                sceneManager.setCurrentScene(Scenes.GAME);
                OpenGL.hideMousePointer();
            } catch (SceneManagerException ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (component.equals(buttonPlay)) {
            if (sketchWars != null) {
                sketchWars.startGame();
            } else {
                System.err.println("Sketchwars instance in the main menu is a null pointer.");
            }
        }  else if (component.equals(buttonOptions)) {
            try {
                sceneManager.setCurrentScene(Scenes.SUB_MENU);
            } catch (SceneManagerException ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }  else if (component.equals(buttonExit)) {
            System.exit(0);
        } 
    }
}

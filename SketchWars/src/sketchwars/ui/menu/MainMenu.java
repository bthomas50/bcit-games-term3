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
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import sketchwars.OpenGL;
import sketchwars.Scenes;
import sketchwars.exceptions.SceneException;
import sketchwars.exceptions.SceneManagerException;
import sketchwars.graphics.GraphicElement;
import sketchwars.graphics.Texture;
import sketchwars.input.Command;
import sketchwars.input.MouseHandler;
import sketchwars.input.MouseState;
import sketchwars.scenes.Layer;
import sketchwars.scenes.Scene;
import sketchwars.scenes.SceneManager;
import sketchwars.ui.Button;

/**
 *
 * @author A00807688
 */
public class MainMenu extends Scene {
    private SceneManager<Scenes> sceneManager;
    
    
    private Texture playBtn;
    private Texture playBtnPress;
    private Texture createBtn;
    private Texture createBtnPress;
    private Texture optionsBtn;
    private Texture optionsBtnPress;
    private Texture exitBtn;
    private Texture exitBtnPress;
    private Texture backgroundImage;
    private Button<Command> createButton;
    private ArrayList<Button> buttonList = new ArrayList<Button>();
    
    private MouseState lastState;
    
    public MainMenu(SceneManager<Scenes> sceneManager) {
        this.sceneManager = sceneManager;

        createLayers();
        createButtons();
        createBackground();
    }
    /*
    background
    Play
    Create
    Options
    Exit
    */
    
    private void createLayers()
    {
        Layer bglayer = new Layer();
        bglayer.setZOrder(-1);
        
        Layer btnLayer = new Layer();
        btnLayer.setZOrder(0);
        
        addLayer(MenuLayers.BACKGROUND, bglayer);
        addLayer(MenuLayers.BUTTONS, btnLayer); 
        
        lastState = MouseHandler.state;
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
            Button<Command> playButton = new Button(new Vector2d(0.03, -0.30),size,playBtn,playBtnPress,Command.MAIN_MENU_PLAY);
            btnLayer.addDrawableObject(playButton);
            
            //create
            createButton = new Button(new Vector2d(0.03, -0.45),size,createBtn,createBtnPress,Command.MAIN_MENU_CREATE);
            btnLayer.addDrawableObject(createButton);            

            //create
            Button<Command> optionsButton = new Button(new Vector2d(0.03, -0.60),size,optionsBtn,optionsBtnPress,Command.MAIN_MENU_OPTIONS);
            btnLayer.addDrawableObject(optionsButton);     
            
            //create
            Button<Command> exitButton = new Button(new Vector2d(0.03, -0.75),size,exitBtn,exitBtnPress,Command.MAIN_MENU_EXIT);
            btnLayer.addDrawableObject(exitButton);     
            
            buttonList.add(playButton);
            buttonList.add(createButton);
            buttonList.add(optionsButton);
            buttonList.add(exitButton);
            
        } catch (SceneException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    private void handleInput(float x, float y)
    {
          for(Button temp : buttonList)
          {
              if(temp.contains(x, y))
              {
                if(temp.getCommand().equals(Command.MAIN_MENU_PLAY))
                {
                  try {
                      sceneManager.setCurrentScene(Scenes.GAME);
                      OpenGL.hideMousePointer();
                  } catch (SceneManagerException ex) {
                      Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                  }
                }
                else if(temp.getCommand().equals(Command.MAIN_MENU_CREATE))
                {
                  try {
                      sceneManager.setCurrentScene(Scenes.GAME);
                      OpenGL.hideMousePointer();
                  } catch (SceneManagerException ex) {
                      Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                  }

                }
                else if(temp.getCommand().equals(Command.MAIN_MENU_OPTIONS))
                {
                  try {
                      sceneManager.setCurrentScene(Scenes.SUB_MENU);
                  } catch (SceneManagerException ex) {
                      Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                  }

                }
                else if(temp.getCommand().equals(Command.MAIN_MENU_EXIT))
                {
                  // kill game
                  System.exit(0);
                }
            } else {
                  temp.setOnPress(false);
              }
        }
          
        
    }
    

    @Override
    public void update(double delta) {
        float x = MouseHandler.getNormalizedX();
        float y = MouseHandler.getNormalizedY();
        
        MouseState currentState = MouseHandler.state;
    
        if (currentState.equals(MouseState.UP) && lastState.equals(MouseState.RISING)) {
            handleInput(x, y);
        } else if (currentState.equals(MouseState.DOWN)) {
            for(Button temp : buttonList) {
                if(temp.contains(x, y)) {
                    temp.setOnPress(true);
                } else {
                    temp.setOnPress(false);
                }
            }
        }
        
        super.update(delta); 
        lastState = MouseHandler.state;
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
    
}

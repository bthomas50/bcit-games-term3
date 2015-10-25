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
import sketchwars.sound.SoundPlayer;
import sketchwars.ui.Button;

/**
 *
 * @author Lightcan
 */
public class OptionMenu extends Scene {
    
    private SceneManager<Scenes> sceneManager;
    private ArrayList<Button> buttonList = new ArrayList<Button>();
    
    private Texture backgroundImage;
    private Texture backBtn;
    private Texture backBtnPress;
    private Texture musicBtn;
    private Texture musicBtnPress;
    
    public OptionMenu(SceneManager<Scenes> sceneManager) {
        this.sceneManager = sceneManager;

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
        backBtn = Texture.loadTexture("content/menu/back.png", true);
        backBtnPress = Texture.loadTexture("content/menu/back_press.png", true);
        
        musicBtn = Texture.loadTexture("content/menu/music.png", true);
        musicBtnPress = Texture.loadTexture("content/menu/music_press.png", true);

        
        Vector2d size = new Vector2d(0.3f,0.12f);

        try {
            Layer btnLayer = getLayer(MenuLayers.BUTTONS);

            //play 
            Button<Command> backButton = new Button(new Vector2d(0.03, -0.30),size,backBtn,backBtnPress,Command.OPTION_MENU_BACK);
            btnLayer.addDrawableObject(backButton);
            
            //create
            Button<Command> musicButton = new Button(new Vector2d(0.03, -0.45),size,musicBtn,musicBtnPress,Command.OPTION_MENU_MUSIC);
            btnLayer.addDrawableObject(musicButton);            

            buttonList.add(backButton);
            buttonList.add(musicButton);

            
        } catch (SceneException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    @Override
    public void update(double delta) {
        
       // System.out.println(MouseHandler.y + "|x;"+ MouseHandler.x);
        if(MouseHandler.state.equals(MouseState.DOWN))
        {
           handleInput();
        }
        super.update(delta); 
        
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
    
    private void handleInput()
    {
        float x = MouseHandler.getNormalizedX();
          float y = MouseHandler.getNormalizedY();

          for(Button temp : buttonList)
          {
              if(temp.contains(x, y))
              {
                if(temp.getCommand().equals(Command.OPTION_MENU_BACK))
                {
                    try 
                    {
                        
                        sceneManager.setCurrentScene(Scenes.MAIN_MENU);
                        //OpenGL.hideMousePointer();
                    } catch (SceneManagerException ex) {
                        Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if(temp.isOnPress())
                    {
                        temp.setOnPress(false);
                    }
                    else
                    {
                        temp.setOnPress(true);
                    }
                }
                  else if(temp.getCommand().equals(Command.OPTION_MENU_MUSIC))
                  {
                    SoundPlayer.pause(0);
                    if(temp.isOnPress())
                    {
                        temp.setOnPress(false);
                    }
                    else
                    {
                        temp.setOnPress(true);
                    }
                    
                      
                  }
              }
          }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.ui.menu;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Vector2d;
import sketchwars.Scenes;
import sketchwars.exceptions.SceneException;
import sketchwars.exceptions.SceneManagerException;
import sketchwars.graphics.GraphicElement;
import sketchwars.graphics.Texture;
import sketchwars.scenes.Layer;
import sketchwars.scenes.Scene;
import sketchwars.scenes.SceneManager;
import sketchwars.sound.SoundPlayer;
import sketchwars.ui.components.Button;
import sketchwars.ui.components.UIActionListener;
import sketchwars.ui.components.UIComponent;

/**
 *
 * @author Lightcan
 */
public class OptionMenu extends Scene implements UIActionListener {
    
    private SceneManager<Scenes> sceneManager;
    
    private Texture backgroundImage;
    private Texture backBtn;
    private Texture backBtnPress;
    private Texture musicBtn;
    private Texture musicBtnPress;
    
    private Button backButton;
    private Button musicButton;
    
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
            backButton = new Button(new Vector2d(0.03, -0.30), size, backBtn, backBtnPress, null);
            btnLayer.addDrawableObject(backButton);
            backButton.addActionListener(this);
            
            //create
            musicButton = new Button(new Vector2d(0.03, -0.45), size, musicBtn, musicBtnPress, null);
            btnLayer.addDrawableObject(musicButton);            
            musicButton.addActionListener(this);
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
        if (component.equals(backButton)) {
            try {
                sceneManager.setCurrentScene(Scenes.MAIN_MENU);
            } catch (SceneManagerException ex) {
                Logger.getLogger(OptionMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (component.equals(backButton)) {
            SoundPlayer.pause(0);
        }
    }
}

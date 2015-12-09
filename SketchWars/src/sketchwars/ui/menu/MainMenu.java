/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.ui.menu;

import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Vector2d;
import sketchwars.Scenes;
import sketchwars.SketchWars;
import sketchwars.exceptions.SceneException;
import sketchwars.exceptions.SceneManagerException;
import sketchwars.graphics.GraphicElement;
import sketchwars.graphics.Texture;
import sketchwars.scenes.*;
import sketchwars.ui.components.ComboBox;
import sketchwars.ui.components.ListBox;
import sketchwars.ui.components.TextButton;
import sketchwars.ui.components.TextInputbox;
import sketchwars.ui.components.UIActionListener;
import sketchwars.ui.components.UIComponent;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class MainMenu extends Scene implements UIActionListener {
    private static final Font font = new Font("Comic Sans MS", Font.ITALIC, 12);
    
    private final SceneManager<Scenes> sceneManager;
    private final SketchWars sketchWars;
    private final LobbyMenu lobby;
    private final FindHostMenu hostlobby;
    
    private Texture normalBtn;
    private Texture hoverBtn;
    private Texture pressBtn;
    private Texture backgroundImage;
    
    private TextButton buttonJoin;
    private TextButton buttonCreate;
    private TextButton buttonTutorial;
    private TextButton buttonOptions;
    private TextButton buttonExit;
    
    
    public MainMenu(SceneManager<Scenes> sceneManager, SketchWars sketchWars, LobbyMenu lobby,
            FindHostMenu hostlobby, Camera camera) {
        super(camera);
        this.sceneManager = sceneManager;
        this.sketchWars = sketchWars;
        this.lobby = lobby;
        this.hostlobby = hostlobby;
        
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
        normalBtn = Texture.loadTexture("content/menu/normal_btns.png", true);
        hoverBtn = Texture.loadTexture("content/menu/hover_btns.png", true);
        pressBtn = Texture.loadTexture("content/menu/click_btns.png", true);

        
        Vector2d size = new Vector2d(0.3f,0.12f);

        try {
            Layer btnLayer = getLayer(MenuLayers.BUTTONS);

            //Join 
            buttonJoin = new TextButton("JOIN",font,new Vector2d(0.03, -0.23), size,normalBtn,hoverBtn,pressBtn);
            btnLayer.addDrawableObject(buttonJoin);
            buttonJoin.addActionListener(this);
                    
            //create
            buttonCreate = new TextButton("CREATE",font,new Vector2d(0.03, -0.37), size,normalBtn,hoverBtn,pressBtn);
            btnLayer.addDrawableObject(buttonCreate);            
            buttonCreate.addActionListener(this);
            
            //Tutorial
            buttonTutorial = new TextButton("TUTORIAL",font,new Vector2d(0.03, -0.51), size,normalBtn,hoverBtn,pressBtn);
            btnLayer.addDrawableObject(buttonTutorial);            
            buttonTutorial.addActionListener(this);
            
            //Options
            buttonOptions = new TextButton("OPTIONS",font,new Vector2d(0.03, -0.65), size,normalBtn,hoverBtn,pressBtn);
            btnLayer.addDrawableObject(buttonOptions);     
            buttonOptions.addActionListener(this);
            
            //Exit
            buttonExit = new TextButton("EXIT",font,new Vector2d(0.03, -0.79), size,normalBtn,hoverBtn,pressBtn);
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
    public void action(UIComponent component, float x, float y) {
        if (component.equals(buttonCreate)) {
            try {
                sceneManager.setCurrentScene(Scenes.GAME_SETTING_MENU);
            } catch (SceneManagerException ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if(component.equals(buttonTutorial)) {
            if (sketchWars != null) {
                sketchWars.startTutorial();
                try {
                    sceneManager.setCurrentScene(Scenes.TUTORIAL_SEQUENCE);
                } catch (SceneManagerException ex) {
                    Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.err.println("Sketchwars instance in the main menu is a null pointer.");
            }
        } else if (component.equals(buttonJoin)) {
            try {
                hostlobby.startServer();
                sceneManager.setCurrentScene(Scenes.FINDHOST_MENU);
            } catch (SceneManagerException ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
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

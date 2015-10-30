
package sketchwars.ui.menu;

import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;
import network.Server;
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
import sketchwars.ui.components.Label;
import sketchwars.ui.components.TextButton;
import sketchwars.ui.components.TextInputbox;
import sketchwars.ui.components.UIActionListener;
import sketchwars.ui.components.UIComponent;
/**
 *
 * @author a00762764
 */
public class CreateOption extends Scene implements UIActionListener{
    
    private SceneManager<Scenes> sceneManager;
    
    private Texture backgroundImage;
    private Texture normalBtn;
    private Texture hoverBtn;
    private Texture pressBtn;
    private Font font;
    
    private TextButton backButton;
    Server server;
    
    public CreateOption(SceneManager<Scenes> sceneManager) {
        this.sceneManager = sceneManager;

        font = new Font("Comic Sans MS", Font.ITALIC, 12);
        createLayers();
        createButtons();
        createBackground();
    }
    
    public CreateOption(SceneManager<Scenes> sceneManager, Server serv) {
        this.sceneManager = sceneManager;
        server = serv;
        font = new Font("Comic Sans MS", Font.ITALIC, 12);
        createLayers();
        createButtons();
        createBackground();
    }
    
    private void setServer(Server server)
    {
        this.server= server;
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

            
            
            //back 
            backButton = new TextButton("BACK",font,new Vector2d(0.03, -0.30), size,normalBtn,hoverBtn,pressBtn);
            btnLayer.addDrawableObject(backButton);
            backButton.addActionListener(this);
            
            //player list
            server.getAllClients();
            Label user = new Label("guy name",font,new Vector2d(0.03, -0.40), size,null);
            btnLayer.addDrawableObject(user);
            
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

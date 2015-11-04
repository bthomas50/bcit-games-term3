
package sketchwars.ui.menu;

import entities.ClientEntityForManagementOnServer;
import java.awt.Color;
import java.awt.Font;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.ServerMain;
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
import sketchwars.ui.components.ComboBox;
import sketchwars.ui.components.Label;
import sketchwars.ui.components.ListBox;
import sketchwars.ui.components.TextButton;
import sketchwars.ui.components.TextInputbox;
import sketchwars.ui.components.UIActionListener;
import sketchwars.ui.components.UIComponent;
import sketchwars.ui.components.UIGroup;
/**
 *
 * @author a00762764
 */
public class GameSettingMenu extends Scene implements UIActionListener{
    
    private SceneManager<Scenes> sceneManager;
    
    private Texture backgroundImage;
    private Texture normalBtn;
    private Texture hoverBtn;
    private Texture pressBtn;
    private Font font;
    private Server server;
    Layer btnLayer;
    
    
    private TextButton backButton;
    private TextButton ContinueButton;
    private TextInputbox b2;

    
    private Collection<ClientEntityForManagementOnServer> userList;
    
    
    public GameSettingMenu(SceneManager<Scenes> sceneManager) {
        this.sceneManager = sceneManager;

        font = new Font("Comic Sans MS", Font.ITALIC, 12);
        createLayers();
        createButtons();
        createBackground();
    }
    
    public GameSettingMenu(SceneManager<Scenes> sceneManager, Server server) {
        this.sceneManager = sceneManager;
        this.server = server;
        font = new Font("Comic Sans MS", Font.ITALIC, 12);
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
            
            btnLayer = getLayer(MenuLayers.BUTTONS);
            
            //StartLobby Button 
            ContinueButton = new TextButton("Continue",font,new Vector2d(0.03, -0.30), size,normalBtn,hoverBtn,pressBtn);
            btnLayer.addDrawableObject(ContinueButton);
            ContinueButton.addActionListener(this);
            
            //Back Button 
            backButton = new TextButton("BACK",font,new Vector2d(0.03, -0.45), size,normalBtn,hoverBtn,pressBtn);
            btnLayer.addDrawableObject(backButton);
            backButton.addActionListener(this);
            
            ////////////////////Setting components////////////////////////////////////
            
            UIGroup group = new UIGroup(new Vector2d(), new Vector2d(2, 2));
            
            //Username  label
            Label userNameLabel = new Label("Username: ",font,new Vector2d(-0.50, 0.8),new Vector2d(0.4, 0.1),null);
            group.addUIComponent(userNameLabel);
            Vector2d size1 = new Vector2d(0.4, 0.1);
            b2 = new TextInputbox(new Vector2d(-0.1, 0.8), size1, null);
            b2.setText("Host");
            b2.setFontColor(Color.BLUE);
            //
            
            //MAPS selections
            Label mapLabel = new Label("Maps: ",font,new Vector2d(-0.6, 0.6),new Vector2d(0.4, 0.1),null);
            group.addUIComponent(mapLabel);
            
            ComboBox cbox = new ComboBox(new Vector2d(-0.3, 0.6), new Vector2d(0.4, 0.1),null);
            cbox.setBackgroundFromColor(Color.ORANGE);
            cbox.getListBox().setBackgroundFromColor(Color.ORANGE);
            
            cbox.addItem("Map 1");
            cbox.addItem("Map 2");
            cbox.addItem("Map 3");
            cbox.setSelection(2);
            group.addUIComponent(cbox);
            //
            
            //MAX players label
            Label maxPlayerLabel = new Label("Max Player: ",font,new Vector2d(0.2, 0.6),new Vector2d(0.4, 0.1),null);
            group.addUIComponent(maxPlayerLabel);
            //MAX player list
            ListBox lb = new ListBox(new Vector2d(0.6, 0.4), new Vector2d(0.4, 0.4),  0.1f, null);
            lb.setFontColor(Color.yellow);
            lb.setSelectionBackgroundColor(Color.RED);
            lb.setBackgroundFromColor(Color.BLACK);
            lb.addItem("2");
            lb.addItem("3");
            lb.addItem("4");
            lb.addActionListener(this);
            

            
            group.addUIComponent(lb);
            group.addUIComponent(b2);
            
            btnLayer.addDrawableObject(group);
            


        } catch (SceneException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
 
            
    private void createBackground() {
        
        backgroundImage = Texture.loadTexture("content/menu/sketchWars2_bg.jpg", false);
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
       if (component.equals(backButton)) 
       {
            try 
            {
                sceneManager.setCurrentScene(Scenes.MAIN_MENU);
            } 
            catch (SceneManagerException ex) 
            {
                Logger.getLogger(OptionMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       else if(component.equals(ContinueButton))
       {
            try 
            {
                ServerMain.setHostUsername(b2.getText());
                sceneManager.setCurrentScene(Scenes.CREATE_MENU);
            } 
            catch (SceneManagerException ex) 
            {
                Logger.getLogger(OptionMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
    
    }
}

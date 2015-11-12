
package sketchwars.ui.menu;

import entities.ClientEntityForManagementOnServer;
import java.awt.Color;
import java.awt.Font;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.ServerMain;
import network.DiscoveryServer;
import network.GameSetting;
import network.Server;
import org.joml.Vector2d;
import sketchwars.Scenes;
import sketchwars.exceptions.SceneException;
import sketchwars.exceptions.SceneManagerException;
import sketchwars.graphics.GraphicElement;
import sketchwars.graphics.Texture;
import sketchwars.scenes.*;
import sketchwars.ui.components.*;
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
    private Server server;
    private Thread discoveryThread;
    private boolean Triger = false;
    Layer btnLayer;
    
    
    private TextButton backButton;
    private TextButton findButton;
    private ListBox userListBox;
    private UIGroup group;
    
    private Collection<ClientEntityForManagementOnServer> userList;
    
    
    public CreateOption(SceneManager<Scenes> sceneManager, Camera camera) {
        super(camera);
        this.sceneManager = sceneManager;

        font = new Font("Comic Sans MS", Font.ITALIC, 12);
        createLayers();
        createButtons();
        createBackground();
    }
    
    public CreateOption(SceneManager<Scenes> sceneManager, Server server, Camera camera) {
        super(camera);
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
            
            group = new UIGroup(new Vector2d(0, 0), new Vector2d(2, 2));
            btnLayer.addDrawableObject(group);
            
            //back 
            backButton = new TextButton("BACK",font,new Vector2d(0.03, -0.30), size,normalBtn,hoverBtn,pressBtn);
            group.addUIComponent(backButton);
            backButton.addActionListener(this);
            
            //find 
            findButton = new TextButton("FIND",font,new Vector2d(0.03, -0.20), size,normalBtn,hoverBtn,pressBtn);
            group.addUIComponent(findButton);
            findButton.addActionListener(this);
            

            //Player List
            
            userListBox = new ListBox(new Vector2d(0.5, 0), new Vector2d(0.4, 0.4),  0.1f, null);
            userListBox.setFontColor(Color.yellow);
            userListBox.setSelectionBackgroundColor(Color.RED);
            userListBox.setBackgroundFromColor(Color.BLACK);
            userListBox.addActionListener(this);
            group.addUIComponent(userListBox);
            
        } catch (SceneException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
   @Override
    public void update(double delta) {
        super.update(delta);

        if(Triger==true)
        {
            //Find latest connected players
            userList = server.getAllClients();
            //Every update clean list values
            userListBox.clearAll();
            Iterator i = userList.iterator();
            while (i.hasNext())
            {
                //Added clients to list 
                ClientEntityForManagementOnServer name = (ClientEntityForManagementOnServer)i.next();
                userListBox.addItem(name.getUsername());
                
            }
            
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
    
    private void startGame()
    {
        server = new Server(6969, new GameSetting());
        
        new Thread(server).start();
        discoveryThread = new DiscoveryServer();
        discoveryThread.start();
        ServerMain.tryToRunClient(server.localAddress, 6969,ServerMain.getHostUsername());

        Triger = true;

    }
    

    @Override
    public void action(UIComponent component, float x, float y) {
       if (component.equals(backButton)) {
            //
            //discoveryThread.
            if(server !=null)
            {
                Triger = false;
                server.stop();
            }
            try 
            {
                sceneManager.setCurrentScene(Scenes.MAIN_MENU);
            } 
            catch (SceneManagerException ex) 
            {
                Logger.getLogger(OptionMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (component.equals(findButton)) {
            startGame();
        }
    
    }
}

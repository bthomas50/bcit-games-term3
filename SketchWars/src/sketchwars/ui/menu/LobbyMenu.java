
package sketchwars.ui.menu;

import entities.ClientEntityForClients;
import entities.ClientEntityForManagementOnServer;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import network.Client;
import network.DiscoveryClient;
import network.DiscoveryServer;
import network.GameSetting;
import network.Server;
import org.joml.Vector2d;
import sketchwars.Scenes;
import sketchwars.SketchWars;
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
public class LobbyMenu extends Scene implements UIActionListener{
    private static final Font font = new Font("Comic Sans MS", Font.ITALIC, 12);
    
    private SceneManager<Scenes> sceneManager;
    
    private Texture backgroundImage;
    private Texture normalBtn;
    private Texture hoverBtn;
    private Texture pressBtn;
    private Server server;
    private DiscoveryServer discoveryServer;
    private Client localClient;
    private DiscoveryClient discoveryClient;
    private String hostUsername = "HOST";
    private GameSetting gameSettings = new GameSetting();
    private Layer btnLayer;
    private final SketchWars game;
    
    private TextButton backButton;
    private ListBox userListBox;
    private UIGroup group;
    
    private Collection<ClientEntityForClients> userList;
    
    
    public LobbyMenu(SceneManager<Scenes> sceneManager, SketchWars game, Camera camera) {
        super(camera);
        this.sceneManager = sceneManager;
        this.game = game;
        createLayers();
        createButtons();
        createBackground();
    }
    
    private void createLayers()
    {
        Layer bglayer = new Layer();
        bglayer.setZOrder(-1);
        
        btnLayer = new Layer();
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
        
        group = new UIGroup(new Vector2d(0, 0), new Vector2d(2, 2));
        btnLayer.addDrawableObject(group);

        //back 
        backButton = new TextButton("CANCEL",font,new Vector2d(0.03, -0.30), size,normalBtn,hoverBtn,pressBtn);
        group.addUIComponent(backButton);
        backButton.addActionListener(this);

        //Player List
        userListBox = new ListBox(new Vector2d(0.5, 0), new Vector2d(0.4, 0.4),  0.1f, null);
        userListBox.setFontColor(Color.yellow);
        userListBox.setSelectionBackgroundColor(Color.RED);
        userListBox.setBackgroundFromColor(Color.BLACK);
        userListBox.addActionListener(this);
        group.addUIComponent(userListBox);

    }
   @Override
    public void update(double delta) {
        super.update(delta);

        //Find latest connected players
        userList = localClient.getConnectedClients();
        //Every update clean list values
        userListBox.clearAll();
        for (ClientEntityForClients name : userList) {
            userListBox.addItem(name.getUsername());
        }
        //if the client has stopped, start the game!
        if(localClient.isReady()) {
            game.startMultiplayer(localClient.networkResult, localClient.rngResult, localClient.settingResult);
        }

    }
    public void setHostUsername(String str) {
        hostUsername = str;
    }
    
    public void setSettings(GameSetting settings) {
        this.gameSettings = settings;
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
    
    public void startServer()
    {
        try {
            server = new Server(gameSettings);
            new Thread(server).start();
            discoveryServer = new DiscoveryServer(server.port);
            discoveryServer.start();
            localClient = new Client(server.localAddress, server.port, hostUsername);
            localClient.run();
        } catch(IOException ex) {
            System.out.println(ex);
        }
    }
    
    public void startClient(InetAddress ip, int port)
    {

        try
        {
            localClient = new Client(ip, port, "test");
            localClient.run();
        }
        catch(IOException ioe)
        {
            System.out.println("Unable to connect to game at " + ip + ":" + port);
        }
        
    }
    //user hit cancel button
    @Override
    public void action(UIComponent component, float x, float y) {
        if(localClient != null)
        {
            localClient.stop();
        }
        if(server !=null)
        {
            server.stop();
            discoveryServer.halt();
        }
        try 
        {
            sceneManager.setCurrentScene(Scenes.MAIN_MENU);
        } 
        catch (SceneManagerException ex) 
        {
            Logger.getLogger(OptionMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

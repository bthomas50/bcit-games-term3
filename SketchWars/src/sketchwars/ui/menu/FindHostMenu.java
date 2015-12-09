/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.ui.menu;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import network.DiscoveryClient;
import org.joml.Vector2d;
import sketchwars.Scenes;
import sketchwars.exceptions.SceneException;
import sketchwars.exceptions.SceneManagerException;
import sketchwars.graphics.GraphicElement;
import sketchwars.graphics.Texture;
import sketchwars.scenes.Camera;
import sketchwars.scenes.Layer;
import sketchwars.scenes.Scene;
import sketchwars.scenes.SceneManager;
import sketchwars.ui.components.Label;
import sketchwars.ui.components.ListBox;
import sketchwars.ui.components.TextButton;
import sketchwars.ui.components.UIActionListener;
import sketchwars.ui.components.UIComponent;
import sketchwars.ui.components.UIGroup;


/**
 *
 * @author Salman Shaharyar
 */
public class FindHostMenu extends Scene implements UIActionListener {
    
    private SceneManager<Scenes> sceneManager;
    
    private Texture backgroundImage;
    private Texture normalBtn;
    private Texture hoverBtn;
    private Texture pressBtn;
    private TextButton backButton;
    private TextButton joinRoomButton;
    private ListBox gamesListListBox;
    private Font font;
    private final ArrayList<String> activeGameList;
    private DiscoveryClient discoveryClient;
    private LobbyMenu lobby;
    private DiscoveryClient.GameListing selectedListing;
 
    
    public FindHostMenu(SceneManager<Scenes> sceneManager, LobbyMenu lobby, Camera camera) {
        super(camera);
        this.sceneManager = sceneManager;
        this.lobby = lobby;
        font = new Font("Comic Sans MS", Font.ITALIC, 12);
        activeGameList = new ArrayList<String>();
        //activeGameList.add("1.2.2");
        
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
 
    public void startServer()
    {
        discoveryClient = new DiscoveryClient();
        discoveryClient.start();
    }    
    
    private void createButtons() {
        //Loading textures
        normalBtn = Texture.loadTexture("content/menu/normal_btns.png", true);
        hoverBtn = Texture.loadTexture("content/menu/hover_btns.png", true);
        pressBtn = Texture.loadTexture("content/menu/click_btns.png", true);
        

        Vector2d size = new Vector2d(0.3f,0.12f);

        try {
            Layer btnLayer = getLayer(MenuLayers.BUTTONS);
            

            //Back Button 
            backButton = new TextButton("BACK",font,new Vector2d(0.03, -0.75), size,normalBtn,hoverBtn,pressBtn);
            btnLayer.addDrawableObject(backButton);
            backButton.addActionListener(this);
            
            //Join room butoon
            joinRoomButton = new TextButton("JOIN",font,new Vector2d(0.03, -0.60), size,normalBtn,hoverBtn,pressBtn);
            btnLayer.addDrawableObject(joinRoomButton);
            joinRoomButton.addActionListener(this);

            
            //group
            UIGroup group = new UIGroup(new Vector2d(), new Vector2d(2, 2));
            

            //Game List
            //Game modes
            Label gameModeLabel = new Label("Open Games",font,new Vector2d(0.0, 0.4),new Vector2d(.8, 0.4),null);
            group.addUIComponent(gameModeLabel);
            gamesListListBox = new ListBox(new Vector2d(0.0, -0.20), new Vector2d(0.6, 0.45),  0.1f, null);
            gamesListListBox.setFontColor(Color.yellow);
            gamesListListBox.setSelectionBackgroundColor(Color.RED);
            gamesListListBox.setBackgroundFromColor(Color.BLACK);
            gamesListListBox.addActionListener(this);
            group.addUIComponent(gamesListListBox);
            
            btnLayer.addDrawableObject(group);
            

        } catch (SceneException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
   @Override
    public void update(double delta) {
        super.update(delta);
        if(discoveryClient != null)
            gameIdCheck();
        //Clear existing box from old host id
        int selected = gamesListListBox.getSelection();
        gamesListListBox.clearAll();
        //for each game in the active game List, so client can select
        for(String game: activeGameList)
        {
            gamesListListBox.addItem(game);
        }
        gamesListListBox.setSelection(selected);
    }

    //Check string agains game mode
    public void gameIdCheck()
    {
        List<DiscoveryClient.GameListing> availableGames = discoveryClient.getAvailableGames();
        activeGameList.clear();
        for(DiscoveryClient.GameListing game : availableGames)
        {
            if(!activeGameList.contains(gamesListListBox.getSelectedItem()))
            {
                activeGameList.add(game.name);
            }
        }
        
        int idx = gamesListListBox.getSelection();
        if(idx != -1)
        {
            System.out.println("Selected: " + idx);
            selectedListing = availableGames.get(idx);
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
        //Check for active game Id
        
        if (component.equals(backButton)) {
            try {
                sceneManager.setCurrentScene(Scenes.MAIN_MENU);
                if(discoveryClient != null)
                    discoveryClient.signalStopListening();
            } catch (SceneManagerException ex) {
                Logger.getLogger(OptionMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (component.equals(joinRoomButton) && 
                   selectedListing != null) {
            try {
                //connect to X game
                sceneManager.setCurrentScene(Scenes.LOBBY_MENU);
            } catch (SceneManagerException ex) {
                Logger.getLogger(FindHostMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
            discoveryClient.signalStopListening();
            System.out.println("join room: " + gamesListListBox.getSelectedItem());
            lobby.startClient(selectedListing.addr, selectedListing.port);
        }
    }
}

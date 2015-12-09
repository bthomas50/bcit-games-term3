
package sketchwars.ui.menu;

import entities.ClientEntityForManagementOnServer;
import java.awt.Color;
import java.awt.Font;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import network.GameSetting;
import org.joml.Vector2d;
import sketchwars.Scenes;
import sketchwars.character.weapon.WeaponSetTypes;
import sketchwars.exceptions.SceneException;
import sketchwars.exceptions.SceneManagerException;
import sketchwars.game.GameModeType;
import sketchwars.game.Maps;
import sketchwars.graphics.GraphicElement;
import sketchwars.graphics.Texture;
import sketchwars.scenes.*;
import sketchwars.ui.components.*;
/**
 *
 * @author a00762764
 */
public class GameSettingMenu extends Scene implements UIActionListener{
    private static final Font font = new Font("Comic Sans MS", Font.ITALIC, 12);
    private final SceneManager<Scenes> sceneManager;
    
    private Texture backgroundImage;
    private Texture normalBtn;
    private Texture hoverBtn;
    private Texture pressBtn;
    Layer btnLayer;

    //Network attritbutes
    private final GameSetting gameSetting;
    
    
    private TextButton backButton;
    private TextButton continueButton;
    private TextInputbox userNameInput;
    private ComboBox mapCheckBox;
    private ComboBox charactorBox;
    private TextInputbox maxPlayerInput;
    private TextInputbox maxHealthInput;
    private TextInputbox maxTurnTimeInput;
    private ListBox gameModeListBox;
    private ListBox weaponSetListBox;

    private final LobbyMenu lobbyMenu;
    
    private Collection<ClientEntityForManagementOnServer> userList;
    
    
    public GameSettingMenu(SceneManager<Scenes> sceneManager, LobbyMenu lobby, Camera camera) {
        super(camera);
        this.sceneManager = sceneManager;
        gameSetting = new GameSetting();
        createLayers();
        createButtons();
        createBackground();
        this.lobbyMenu = lobby;
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
            continueButton = new TextButton("Continue",font,new Vector2d(0.03, -0.60), size,normalBtn,hoverBtn,pressBtn);
            btnLayer.addDrawableObject(continueButton);
            continueButton.addActionListener(this);
            
            //Back Button 
            backButton = new TextButton("BACK",font,new Vector2d(0.03, -0.75), size,normalBtn,hoverBtn,pressBtn);
            btnLayer.addDrawableObject(backButton);
            backButton.addActionListener(this);
            
            ////////////////////Setting components////////////////////////////////////
            
            UIGroup group = new UIGroup(new Vector2d(), new Vector2d(2, 2));
            
            //Username  label
            Label userNameLabel = new Label("Username: ",font,new Vector2d(-0.80, 0.8),new Vector2d(0.4, 0.1),null);
            group.addUIComponent(userNameLabel);
            userNameInput = new TextInputbox(new Vector2d(-0.4, 0.8), new Vector2d(0.4, 0.1), null);
            userNameInput.setText("Host");
            userNameInput.setFontColor(Color.black);
            //

            //MAPS selections
            Label mapLabel = new Label("Maps: ",font,new Vector2d(-0.85, 0.6),new Vector2d(0.4, 0.1),null);
            group.addUIComponent(mapLabel);
            
            mapCheckBox = new ComboBox(new Vector2d(-0.4, 0.6), new Vector2d(0.4, 0.1),null);
            mapCheckBox.setBackgroundFromColor(Color.WHITE);
            mapCheckBox.getListBox().setBackgroundFromColor(Color.WHITE);
            
            mapCheckBox.addItem("Normal");
            //mapCheckBox.addItem("Rapid");
            mapCheckBox.setSelection(0);
            group.addUIComponent(mapCheckBox);
            //
            
            //Max Character selections
            Label maxCharacterLabel = new Label("Characters: ",font,new Vector2d(-0.8, 0.15),new Vector2d(0.4, 0.1),null);
            group.addUIComponent(maxCharacterLabel);
            
            charactorBox = new ComboBox(new Vector2d(-0.4, 0.15), new Vector2d(0.4, 0.1),null);
            charactorBox.setBackgroundFromColor(Color.WHITE);
            charactorBox.getListBox().setBackgroundFromColor(Color.WHITE);
            
            charactorBox.addItem("1");
            charactorBox.addItem("2");
            charactorBox.addItem("3");
            charactorBox.addItem("4");
            charactorBox.setSelection(1);

            
            //Right side

            //Player label
            Label maxPlayerLabel = new Label("Player: ",font,new Vector2d(0.4, 0.75),new Vector2d(0.4, 0.1),null);
            group.addUIComponent(maxPlayerLabel);
            maxPlayerInput = new TextInputbox(new Vector2d(0.7, 0.75), new Vector2d(0.4, 0.1), null);
            maxPlayerInput.setText("2");
            maxPlayerInput.setFontColor(Color.black);
            //
            
            //Health label
            Label maxHealthLabel = new Label("Health: ",font,new Vector2d(0.4, 0.65),new Vector2d(0.4, 0.1),null);
            group.addUIComponent(maxHealthLabel);
            maxHealthInput = new TextInputbox(new Vector2d(0.7, 0.65), new Vector2d(0.4, 0.1), null);
            maxHealthInput.setText("200");
            maxHealthInput.setFontColor(Color.black);
            //
            
            //Turn Time label
            Label maxTurnTimeLabel = new Label("Time: ",font,new Vector2d(0.4, 0.55),new Vector2d(0.4, 0.1),null);
            group.addUIComponent(maxTurnTimeLabel);
            maxTurnTimeInput = new TextInputbox(new Vector2d(0.7, 0.55), new Vector2d(0.4, 0.1), null);
            maxTurnTimeInput.setText("15");
            maxTurnTimeInput.setFontColor(Color.black);
            //
            

            //Game modes
            Label gameModeLabel = new Label("Mode: ",font,new Vector2d(0.35, -0.10),new Vector2d(0.4, 0.1),null);
            group.addUIComponent(gameModeLabel);
            gameModeListBox = new ListBox(new Vector2d(0.7, -0.20), new Vector2d(0.4, 0.25),  0.1f, null);
            gameModeListBox.setFontColor(Color.black);
            gameModeListBox.setSelectionBackgroundColor(Color.RED);
            gameModeListBox.setBackgroundFromColor(Color.WHITE);
            gameModeListBox.addItem("Normal");
            gameModeListBox.addItem("Rapid Fire");
            gameModeListBox.setSelection(0);
            gameModeListBox.addActionListener(this);
            //
            
            
            //Weapon set list
            Label weaponSetLabel = new Label("Gun Set: ",font,new Vector2d(0.35, 0.40),new Vector2d(0.4, 0.1),null);
            group.addUIComponent(weaponSetLabel);
            weaponSetListBox = new ListBox(new Vector2d(0.7, 0.25), new Vector2d(0.4, 0.4),  0.1f, null);
            weaponSetListBox.setFontColor(Color.black);
            weaponSetListBox.setSelectionBackgroundColor(Color.RED);
            weaponSetListBox.setBackgroundFromColor(Color.WHITE);
            weaponSetListBox.addItem("MELEE");
            weaponSetListBox.addItem("RANGE");
            weaponSetListBox.addItem("EXPLOSIVE");
            weaponSetListBox.addItem("MIX");
            weaponSetListBox.setSelection(0);
            weaponSetListBox.addActionListener(this);
            //

            //Draw them
            group.addUIComponent(weaponSetListBox);
            group.addUIComponent(gameModeListBox);
            group.addUIComponent(charactorBox);
            group.addUIComponent(maxTurnTimeInput);
            group.addUIComponent(maxPlayerInput);
            group.addUIComponent(maxHealthInput);
            group.addUIComponent(userNameInput);
            
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
    
    private void createGameSetting(GameSetting setting)
    {

        //Health
        String temp = maxHealthInput.getText();
        setting.setMaxPlayer(Integer.parseInt(temp));
        //Turn Delay
        temp = maxTurnTimeInput.getText();
        setting.setTimePerTurn(Integer.parseInt(temp));
        //Player limit
        temp = maxPlayerInput.getText();
        setting.setMaxPlayer(Integer.parseInt(temp));
        //Character limit
        temp = charactorBox.getSelectedItem();
        setting.setCharacterPerTeam(Integer.parseInt(temp));
        //Map
        temp = mapCheckBox.getSelectedItem();
        setting.setMapSelected(mapCheck(temp));
        
        //Game Mode
        temp = gameModeListBox.getSelectedItem();
        setting.setGameModeSelected(gameModeCheck(temp));

        //Weapon Set 
        temp = weaponSetListBox.getSelectedItem();
        setting.setWeaponSetSelected(weaponModeCheck(temp));
       
    }

    //Check string against weapon set
    public WeaponSetTypes weaponModeCheck(String value)
    {
        if(value.equalsIgnoreCase("Melee"))
        {
            return WeaponSetTypes.MELEE; 
        }
        else if(value.equalsIgnoreCase("Range"))
        {
            return WeaponSetTypes.RANGE;
        }
        else if(value.equalsIgnoreCase("Explosive"))
        {
            return WeaponSetTypes.EXPLOSIVE;
        }
        else
        {
            return WeaponSetTypes.MIX;
        }
    }
    //Check string agains game mode
    public GameModeType gameModeCheck(String value)
    {
        if(value.equalsIgnoreCase("normal"))
        {
            return GameModeType.Normal; 
        }
        else if(value.equalsIgnoreCase("Rapid Fire"))
        {
            return GameModeType.RAPID_FIRE;
        }
        else
        {
            return GameModeType.Normal;
        }
    }
    //Check string agains game mode
    public Maps mapCheck(String value)
    {
        if(value.equalsIgnoreCase("normal"))
        {
            return Maps.NORMAL; 
        }
        else
        {
            return Maps.NORMAL;
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
       else if(component.equals(continueButton))
       {
            try 
            {
                createGameSetting(gameSetting);
                //Set Host Name
                lobbyMenu.setHostUsername(userNameInput.getText());
                //GameSettingFileUpdate
                lobbyMenu.setSettings(gameSetting);
                lobbyMenu.startServer();
                //Set screen to lobby screen
                sceneManager.setCurrentScene(Scenes.LOBBY_MENU);
            } 
            catch (SceneManagerException ex) 
            {
                Logger.getLogger(OptionMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
    }
}

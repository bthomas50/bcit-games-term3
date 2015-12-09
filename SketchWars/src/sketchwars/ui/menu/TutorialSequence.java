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
import sketchwars.input.Command;
import sketchwars.input.CommandType;
import sketchwars.input.Input;
import sketchwars.scenes.Camera;
import sketchwars.scenes.Layer;
import sketchwars.scenes.Scene;
import sketchwars.scenes.SceneManager;

/**
 *
 * @author a00861166
 */
public class TutorialSequence extends Scene {
    private final Texture controlsImage;
    private final SceneManager sm;
    
    public TutorialSequence(SceneManager sm, Camera camera) {
        super(camera);
        this.sm = sm;
        controlsImage = Texture.loadTexture("content/menu/controls.png", true);
        createLayers();
        initBackground();
    }
    
    private void createLayers()
    {
        Layer bglayer = new Layer();
        bglayer.setZOrder(0);
        
        addLayer(MenuLayers.BACKGROUND, bglayer);
    }
    
    private void initBackground() 
    {
        try 
        {
            Layer bgLayer = getLayer(MenuLayers.BACKGROUND);
            GraphicElement bg = new GraphicElement(new Vector2d(0,0),new Vector2d(2,2),controlsImage);
            bgLayer.addDrawableObject(bg);
        } 
        catch (SceneException ex) 
        {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void update(double delta) {
        super.update(delta);
        for(Command comm : Input.localInput.getCommands())
        {
            if(comm.getType() == CommandType.CONTINUE)
            {
                try {
                    sm.setCurrentScene(Scenes.GAME);
                } catch (SceneManagerException ex) {
                    Logger.getLogger(TutorialSequence.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            if(comm.getType() == CommandType.SHOWMENU)
            {
                try {
                    sm.setCurrentScene(Scenes.MAIN_MENU);
                } catch (SceneManagerException ex) {
                    Logger.getLogger(TutorialSequence.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.ui.menu;

import sketchwars.Scenes;
import sketchwars.scenes.Scene;
import sketchwars.scenes.SceneManager;

/**
 *
 * @author A00807688
 */
public class MainMenu {
    private SceneManager<Scenes> sceneManager;
    private Scene<MenuLayers> menuScene;
    
    public MainMenu(Scene menuScene, SceneManager<Scenes> sceneManager) {
        this.menuScene = menuScene;
        this.sceneManager = sceneManager;
        
        createButtons();
    }

    private void createButtons() {
        //bakc to game sceneManager.setCurrentScene(Scenes.GAME);
    }
    
}

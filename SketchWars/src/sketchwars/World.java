/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars;

import java.util.logging.Level;
import java.util.logging.Logger;
import sketchwars.SketchWars.Scenes;
import sketchwars.character.Character;
import sketchwars.character.projectiles.AbstractProjectile;
import sketchwars.character.projectiles.TestBullet;
import sketchwars.character.weapon.AbstractWeapon;
import sketchwars.character.weapon.TestWeapon;
import sketchwars.exceptions.SceneMangerException;
import sketchwars.map.AbstractMap;
import sketchwars.map.TestMap;
import sketchwars.scenes.GameScene;
import sketchwars.scenes.SceneManager;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class World {
    private final SceneManager sceneManager;
        
    private AbstractMap map;
    private Character character;
    
    AbstractWeapon weaponTest;
    AbstractProjectile projectileTest;
            
    public World(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }
    
    public void init() {        

        map = new TestMap();
        map.init();

        character = new Character();
        character.init();

        weaponTest = new TestWeapon();
        weaponTest.init();
        character.setWeapon(weaponTest);

        projectileTest = new TestBullet();
        projectileTest.init();
        projectileTest.setPosition(0.3, 0.3);

        try {
            GameScene gameScene = (GameScene) sceneManager.getScene(Scenes.GAME.ordinal());
            
            gameScene.AddDrwableObject(map);
            gameScene.AddDrwableObject(character);
            gameScene.AddDrwableObject(projectileTest);
        } catch (SceneMangerException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    public void update(double elapsed) {
        
    }
    
    public void dispose() {
        weaponTest.dispose();
        character.dispose();
        projectileTest.dispose();
    }
}

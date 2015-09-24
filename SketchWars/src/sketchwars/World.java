/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWKeyCallback;

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
import sketchwars.input.KeyboardHandler;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class World {
    private final SceneManager<Scenes> sceneManager;
        
    private AbstractMap map;
    private Character character;
    
    AbstractWeapon weaponTest;
    AbstractProjectile projectileTest;
            
    public World(SceneManager<Scenes> sceneManager) {
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
            GameScene gameScene = (GameScene) sceneManager.getScene(Scenes.GAME);
            
            gameScene.AddDrwableObject(map);
            gameScene.AddDrwableObject(character);
            gameScene.AddDrwableObject(projectileTest);
        } catch (SceneMangerException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    /**
     * 
     * @param delta frame length in millisecond
     */
     public void update(double delta) {
         
         /* Dont know where to put this code. Possibly character though
            we may need a distinction between client and peer characters */
        if(KeyboardHandler.isKeyDown(GLFW_KEY_W)){
            System.out.println("W is pressed");
        } else if(KeyboardHandler.isKeyDown(GLFW_KEY_S)){
            System.out.println("S is pressed");
        }
        
        if(KeyboardHandler.isKeyDown(GLFW_KEY_A)){
            System.out.println("A is pressed");
        } else if(KeyboardHandler.isKeyDown(GLFW_KEY_D)){
            System.out.println("D is pressed");
        }
        
        if(KeyboardHandler.isKeyDown(GLFW_KEY_SPACE))
            System.out.println("W is pressed");
        
        if(KeyboardHandler.isKeyDown(GLFW_KEY_SPACE)){
            System.out.println("Space is pressed");
        }
        
        if(KeyboardHandler.isKeyDown(GLFW_KEY_UP)){
            //increment angle
        } else if(KeyboardHandler.isKeyDown(GLFW_KEY_DOWN)){
            //decrement angle
        }
    }
    
    public void dispose() {
        weaponTest.dispose();
        character.dispose();
        projectileTest.dispose();
    }
}

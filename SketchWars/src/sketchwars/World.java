/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars;

import sketchwars.graphics.Graphics;
import sketchwars.character.Character;
import sketchwars.character.projectiles.AbstractProjectile;
import sketchwars.character.projectiles.TestBullet;
import sketchwars.character.weapon.AbstractWeapon;
import sketchwars.character.weapon.TestWeapon;
import sketchwars.map.AbstractMap;
import sketchwars.map.TestMap;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class World {
    private final Graphics graphicsObjs;
    
    private AbstractMap map;
    private Character character;
    
    AbstractWeapon weaponTest;
    AbstractProjectile projectileTest;
            
    public World(Graphics graphicsObjs) {
        this.graphicsObjs = graphicsObjs;
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
        
        graphicsObjs.AddDrwableObject(map);
        graphicsObjs.AddDrwableObject(character);
        graphicsObjs.AddDrwableObject(projectileTest);
    }
    
    public void update(double elapsed) {
        
    }
    
    public void dispose() {
        weaponTest.dispose();
        character.dispose();
        projectileTest.dispose();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.character;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class CharacterTest {
    @Test
    public void testCreation() {
        SketchCharacter character1 = new SketchCharacter();
        assertEquals(SketchCharacter.DEFAULT_MAX_HEALTH, character1.getHealth());
        assertEquals(SketchCharacter.DEFAULT_MAX_HEALTH, character1.getMaxHealth());
        
        int maxHealth = 200;
        int startHealth = 100;
        SketchCharacter character2 = new SketchCharacter(maxHealth, startHealth);
        
        assertEquals(startHealth, character2.getHealth());
        assertEquals(maxHealth, character2.getMaxHealth());
        
    }
    
    @Test
    public void testDamageTaking() {
        SketchCharacter character1 = new SketchCharacter(200, 100);
        
        character1.takeDamage(50);
        assertEquals(50, character1.getHealth());
        
        character1.takeDamage(1000);
        assertEquals(0, character1.getHealth());
        assertTrue(character1.isDead());
    }
    
    @Test
    public void testHealing() {
        int maxHealth = 300;
        int startHealth = 100;
        SketchCharacter character1 = new SketchCharacter(maxHealth, startHealth);
         
        character1.heal(50);
        assertEquals(startHealth + 50, character1.getHealth());
        
        character1.heal(1000);
        assertEquals(maxHealth, character1.getHealth());
    }
}

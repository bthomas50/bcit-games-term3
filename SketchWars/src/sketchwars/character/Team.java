package sketchwars.character;

import sketchwars.GameObject;
import sketchwars.character.Character;
import sketchwars.character.weapon.AbstractWeapon;
import sketchwars.input.KeyboardHandler;

import java.util.*;
import static org.lwjgl.glfw.GLFW.*;

/**
 *
 * @author David Ly <ly_nekros@hotmail.com>
 */
public class Team 
{
    private ArrayList<Character> characters;
    private HashMap<AbstractWeapon.WeaponEnum, AbstractWeapon> weapons;
    private Character active;
    
    public Team(ArrayList<Character> characters, HashMap<AbstractWeapon.WeaponEnum, AbstractWeapon> weapons)
    {
        this.characters = characters;
        this.weapons = weapons;
        if(this.characters.size() > 0)
        {
            active = this.characters.get(0);
        }
    }

    public void changeAmmo(AbstractWeapon.WeaponEnum weaponType, int num)
    {
        weapons.get(weaponType).setAmmo(num);
    }
    
    public int getTotalHealth()
    {
        int total = 0;
        for (int i = 0; i < size(); i++)
            total += characters.get(i).getHealth();
        
        return total;
    }

    public void handleInput(double elapsedMillis)
    {
        if(KeyboardHandler.isKeyDown(GLFW_KEY_W)){
            //?
        } else if(KeyboardHandler.isKeyDown(GLFW_KEY_S)){
            //?
        }
        
        if(KeyboardHandler.isKeyDown(GLFW_KEY_A)){
            //active.moveLeft();
        } else if(KeyboardHandler.isKeyDown(GLFW_KEY_D)){
            //active.moveRight();
        }
        
        if(KeyboardHandler.isKeyDown(GLFW_KEY_SPACE)){
            active.fireCurrentWeapon(1.0);
        }
        
        if(KeyboardHandler.isKeyDown(GLFW_KEY_UP)){
            active.aimUp(elapsedMillis);
        } else if(KeyboardHandler.isKeyDown(GLFW_KEY_DOWN)){
            active.aimDown(elapsedMillis);
        }
    }

    public boolean isDead()
    {
        for(Character character : characters)
        {
            System.out.println(character.isDead());
            if(!character.isDead())
                return false;
        }
        return true;
    }
    
    public int size()
    {
        return characters.size();
    }

    public Character getActiveCharacter()
    {
        return active;
    }

    public void incrementActiveCharacter()
    {
        int idx = characters.indexOf(active);
        if(idx != size() - 1)
        {
            active = characters.get(0);
        }
        else
        {
            active = characters.get(idx + 1);
        }
    }
}

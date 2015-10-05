/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.character;
import sketchwars.GameObject;
import sketchwars.character.Character;
import sketchwars.character.weapon.AbstractWeapon;
import java.util.*;
/**
 *
 * @author David Ly <ly_nekros@hotmail.com>
 */
public class Team {
    private ArrayList<Character> characterList;
    private HashMap<AbstractWeapon.WeaponEnum, Integer> weaponList;
    
    public Team()
    {
        characterList = new ArrayList();
        weaponList = new HashMap(10);
    }
    
    public Team(ArrayList characters)
    {
        characterList = characters;
        weaponList = new HashMap(10);
        init_weaponList();
    }
    public Team(ArrayList characters, HashMap weapons)
    {
        characterList = characters;
        weaponList = weapons;    
    }
    private void init_weaponList ()
    {
        weaponList.put(AbstractWeapon.WeaponEnum.MELEE_WEAPON, 99);
        weaponList.put(AbstractWeapon.WeaponEnum.RANGED_WEAPON, 99);
        weaponList.put(AbstractWeapon.WeaponEnum.BASIC_GRENADE, 99);
    }
    public void changeAmmo(AbstractWeapon.WeaponEnum weaponType, int num)
    {
        weaponList.put(weaponType, weaponList.get(weaponType) + num);
    }
    
    public int getCurrentHealth()
    {
        int total = 0;
        for (int i = 0; i < characterList.size(); i++)
            total += characterList.get(i).getHealth();
        
        return total;
    }
    
    public boolean isDead()
    {
        for(Character character : characterList)
        {
            if(!character.isDead())
                return false;
        }
        return true;
    }
    
}

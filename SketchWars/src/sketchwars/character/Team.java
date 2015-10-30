package sketchwars.character;

import sketchwars.character.weapon.AbstractWeapon;
import sketchwars.character.weapon.WeaponTypes;
import sketchwars.input.*;
import sketchwars.HUD.HealthBar;
import java.util.*;

/**
 *
 * @author David Ly <ly_nekros@hotmail.com>
 */
public class Team 
{
    private ArrayList<SketchCharacter> characters;
    private HashMap<WeaponTypes, AbstractWeapon> weapons;
    private SketchCharacter active;
    private HealthBar healthBar;
    
    public Team(ArrayList<SketchCharacter> characters, HashMap<WeaponTypes, AbstractWeapon> weapons)
    {
        this.characters = characters;
        this.weapons = weapons;
        healthBar = new HealthBar();
        if(this.characters.size() > 0)
        {
            setActiveCharacter(this.characters.get(0));
        }
    }
    
    public void setHealthBar (HealthBar healthbar)
    {
        healthBar = healthbar;
    }
    public void changeAmmo(WeaponTypes weaponType, int num)
    {
        weapons.get(weaponType).setAmmo(num);
    }
    
    public void updateTotalHealth()
    {
        int total = 0;
        for (int i = 0; i < characters.size(); i++)
            total += characters.get(i).getHealth();
        
        healthBar.setHealth(total);
    }

    public void handleInput(Input input, double elapsedMillis)
    {
        //if it shot already, its turn is over.
        if(active == null || active.isDead() || active.hasFiredThisTurn())
        {
            return;
        }
        for(Command command : input.getCommands())
        {
            switch(command)
            {
            case FIRE:
                active.fireCurrentWeapon(1.0f);
                break;
            case AIM_UP:
                active.aimUp(elapsedMillis);
                break;
            case AIM_DOWN:
                active.aimDown(elapsedMillis);
                break;
            case MOVE_LEFT:
               active.moveLeft(elapsedMillis);
                break;
            case MOVE_RIGHT:
               active.moveRight(elapsedMillis);
                break;
            case JUMP:
                active.jump(elapsedMillis);
                break;
            case SWITCH_1:
                active.setWeapon(weapons.get(WeaponTypes.MELEE_WEAPON));
                break;
            case SWITCH_2:
                active.setWeapon(weapons.get(WeaponTypes.RANGED_WEAPON));
                break;
            case SWITCH_3:
                active.setWeapon(weapons.get(WeaponTypes.BASIC_GRENADE));
                break;
            case SWITCH_4:
                active.setWeapon(weapons.get(WeaponTypes.MINE));
                break;
            case SWITCH_5:
                active.setWeapon(weapons.get(WeaponTypes.CLUSTER_BOMB));
                break;
            case SWITCH_6:
                active.setWeapon(weapons.get(WeaponTypes.BAZOOKA));
                break;
            case SWITCH_7:
                active.setWeapon(weapons.get(WeaponTypes.ERASER));
                break;
            case SWITCH_8:
                active.setWeapon(weapons.get(WeaponTypes.PENCIL));
                break;
            }
        
        }
    }

    public boolean isDead()
    {
        for(SketchCharacter character : characters)
        {
           // System.out.println(character.isDead());
            if(!character.isDead())
                return false;
        }
        return true;
    }
    
    public int size()
    {
        return characters.size();
    }

    public SketchCharacter getActiveCharacter()
    {
        return active;
    }

    public void cycleActiveCharacter()
    {
        int startIdx = characters.indexOf(active);
        int curIdx = startIdx;
        do
        {
            curIdx = getNextIdx(curIdx);
            SketchCharacter trial = characters.get(curIdx);
            if(!trial.isDead())
            {
                setActiveCharacter(trial);
                break;
            }
        } while(curIdx != startIdx);
    }

    private int getNextIdx(int curIdx) 
    {
        if(curIdx == size() - 1)
        {
            return 0;
        }
        else
        {
            return curIdx+1;
        }
    }

    private void setActiveCharacter(SketchCharacter ch)
    {
        AbstractWeapon wep = getActiveWeapon();
        if(wep == null)
        {
            wep = weapons.get(WeaponTypes.MELEE_WEAPON);
        }
        resetCharacters();
        active = ch;
        active.setWeapon(wep);
    }

    private AbstractWeapon getActiveWeapon()
    {
        if(active != null)
        {
            return active.getWeapon();
        }
        return null;
    }

    private void resetCharacters()
    {
        for(SketchCharacter temp : characters)
        {
            temp.resetHasFiredThisTurn();
            temp.setWeapon(null);
        }
    }
}

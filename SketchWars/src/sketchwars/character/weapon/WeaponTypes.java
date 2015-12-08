package sketchwars.character.weapon;

import java.util.Map;

/**
 *
 * @author Nekros
 */
public enum WeaponTypes 
{
    MELEE_WEAPON,
    RANGED_WEAPON,
    BAZOOKA,
    BASIC_GRENADE,
    CLUSTER_BOMB,
    MINE,
    ERASER,
    PENCIL;
    
    public static AbstractWeapon getDefaultWeapon(Map<WeaponTypes, AbstractWeapon> weaponSet) 
    {
        for(WeaponTypes type : values())
        {
            if(weaponSet.containsKey(type)) 
            {
                return weaponSet.get(type);
            }
        }
        return null;
    }
}

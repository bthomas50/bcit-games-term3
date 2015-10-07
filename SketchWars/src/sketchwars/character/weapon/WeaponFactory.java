package sketchwars.character.weapon;

import sketchwars.graphics.*;
import sketchwars.character.projectiles.ProjectileFactory;

import java.util.HashMap;

public class WeaponFactory
{
    public static HashMap<WeaponEnum, AbstractWeapon> createDefaultWeaponSet(ProjectileFactory fact) 
    {
        HashMap<WeaponEnum, AbstractWeapon> ret = new HashMap<>();
        ret.put(WeaponEnum.BASIC_GRENADE, createGrenade(fact));
        ret.put(WeaponEnum.MELEE_WEAPON, createBoxingGlove(fact));
        ret.put(WeaponEnum.RANGED_WEAPON, createRifle(fact));
        return ret;
    }

    public static AbstractWeapon createGrenade(ProjectileFactory fact)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/grenade.png");
        AbstractWeapon weapon = new GrenadeWeapon(texture, 0.1, fact);
        weapon.setAmmo(AbstractWeapon.INFINITE_AMMO);
        return weapon;
    }

    public static AbstractWeapon createBoxingGlove(ProjectileFactory fact)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/meleeBoxing.png");
        AbstractWeapon weapon = new MeleeWeapon(texture, 0.2, fact);
        weapon.setAmmo(AbstractWeapon.INFINITE_AMMO);
        return weapon;
    }

    public static AbstractWeapon createRifle(ProjectileFactory fact)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/rifle1.png");
        AbstractWeapon weapon = new RangedWeapon(texture, 0.5, fact);
        weapon.setAmmo(AbstractWeapon.INFINITE_AMMO);
        return weapon;
    }

    private WeaponFactory() {}
}
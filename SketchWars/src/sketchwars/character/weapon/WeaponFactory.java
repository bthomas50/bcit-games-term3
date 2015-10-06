package sketchwars.character.weapon;

import sketchwars.graphics.*;

import java.util.HashMap;

public class WeaponFactory
{
    public static HashMap<AbstractWeapon.WeaponEnum, AbstractWeapon> createDefaultWeaponSet() 
    {
        HashMap<AbstractWeapon.WeaponEnum, AbstractWeapon> ret = new HashMap<>();
        ret.put(AbstractWeapon.WeaponEnum.BASIC_GRENADE, createGrenade());
        ret.put(AbstractWeapon.WeaponEnum.MELEE_WEAPON, createBoxingGlove());
        ret.put(AbstractWeapon.WeaponEnum.RANGED_WEAPON, createRifle());
        return ret;
    }

    public static AbstractWeapon createGrenade()
    {
        Texture texture = new Texture();
        texture.loadTexture("content/char/weapons/grenade.png");
        AbstractWeapon weapon = new GrenadeWeapon(texture, 0.1);
        weapon.setAmmo(AbstractWeapon.INFINITE_AMMO);
        return weapon;
    }

    public static AbstractWeapon createBoxingGlove()
    {
        Texture texture = new Texture();
        texture.loadTexture("content/char/weapons/meleeBoxing.png");
        AbstractWeapon weapon = new MeleeWeapon(texture, 0.1);
        weapon.setAmmo(AbstractWeapon.INFINITE_AMMO);
        return weapon;
    }

    public static AbstractWeapon createRifle()
    {
        Texture texture = new Texture();
        texture.loadTexture("content/char/weapons/rifle1.png");
        AbstractWeapon weapon = new RangedWeapon(texture, 0.1);
        weapon.setAmmo(AbstractWeapon.INFINITE_AMMO);
        return weapon;
    }

    private WeaponFactory() {}
}
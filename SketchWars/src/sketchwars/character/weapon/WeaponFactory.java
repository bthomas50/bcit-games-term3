package sketchwars.character.weapon;

import sketchwars.graphics.*;
import sketchwars.character.projectiles.ProjectileFactory;

import java.util.HashMap;

public class WeaponFactory
{
    public static final float GRENADE_SCALE = 0.04f;
    public static final float MELEE_SCALE = 0.052f;
    public static final float RIFLE_SCALE = 0.1f;
        
    public static HashMap<WeaponTypes, AbstractWeapon> createDefaultWeaponSet(ProjectileFactory fact) 
    {
        HashMap<WeaponTypes, AbstractWeapon> ret = new HashMap<>();
        ret.put(WeaponTypes.BASIC_GRENADE, createGrenade(fact));
        ret.put(WeaponTypes.MELEE_WEAPON, createBoxingGlove(fact));
        ret.put(WeaponTypes.RANGED_WEAPON, createRifle(fact));
        return ret;
    }

    public static AbstractWeapon createGrenade(ProjectileFactory fact)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/grenade.png", true);
        
        float ratio = texture.getTextureHeight()/texture.getTextureWidth();
        float width = GRENADE_SCALE;
        float height = width * ratio;
        
        AbstractWeapon weapon = new GrenadeWeapon(texture, width, height, fact);
        weapon.setAmmo(AbstractWeapon.INFINITE_AMMO);
        return weapon;
    }

    public static AbstractWeapon createBoxingGlove(ProjectileFactory fact)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/meleeBoxing.png", true);
        
        float ratio = texture.getTextureHeight()/texture.getTextureWidth();
        float width = MELEE_SCALE;
        float height = width * ratio;
        
        AbstractWeapon weapon = new MeleeWeapon(texture, width, height, fact);
        weapon.setAmmo(AbstractWeapon.INFINITE_AMMO);
        return weapon;
    }

    public static AbstractWeapon createRifle(ProjectileFactory fact)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/rifle1.png", true);
        
        float ratio = texture.getTextureHeight()/texture.getTextureWidth();
        float width = RIFLE_SCALE;
        float height = width * ratio;
        
        AbstractWeapon weapon = new RangedWeapon(texture, width, height, fact);
        weapon.setAmmo(AbstractWeapon.INFINITE_AMMO);
        return weapon;
    }

    private WeaponFactory() {}
}
package sketchwars.character.weapon;

import sketchwars.graphics.*;
import sketchwars.character.projectiles.ProjectileFactory;

import java.util.HashMap;
import sketchwars.OpenGL;

public class WeaponFactory
{
    private static final double GRENADE_SCALE = 0.000012;
    private static final double RIFLE_SCALE = 0.00003;
    private static final double MELEE_SCALE = 0.000015;
    
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
        Texture texture = Texture.loadTexture("content/char/weapons/grenade.png", false);
        
        double ratio = texture.getTextureHeight()/texture.getTextureWidth();
        double widthP = OpenGL.WIDTH * GRENADE_SCALE;
        double heightP = widthP * ratio;
        
        AbstractWeapon weapon = new GrenadeWeapon(texture, widthP, heightP, fact);
        weapon.setAmmo(AbstractWeapon.INFINITE_AMMO);
        return weapon;
    }

    public static AbstractWeapon createBoxingGlove(ProjectileFactory fact)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/meleeBoxing.png", false);
        
        double ratio = texture.getTextureHeight()/texture.getTextureWidth();
        double widthP = OpenGL.WIDTH * MELEE_SCALE;
        double heightP = widthP * ratio;
        
        AbstractWeapon weapon = new MeleeWeapon(texture, widthP, heightP, fact);
        weapon.setAmmo(AbstractWeapon.INFINITE_AMMO);
        return weapon;
    }

    public static AbstractWeapon createRifle(ProjectileFactory fact)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/rifle1.png", false);
        
        double ratio = texture.getTextureHeight()/texture.getTextureWidth();
        double widthP = OpenGL.WIDTH * RIFLE_SCALE;
        double heightP = widthP * ratio;
        
        AbstractWeapon weapon = new RangedWeapon(texture, widthP, heightP, fact);
        weapon.setAmmo(AbstractWeapon.INFINITE_AMMO);
        return weapon;
    }

    private WeaponFactory() {}
}
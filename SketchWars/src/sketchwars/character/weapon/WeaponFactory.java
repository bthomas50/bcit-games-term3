package sketchwars.character.weapon;

import java.awt.image.BufferedImage;
import java.io.IOException;
import sketchwars.graphics.*;
import sketchwars.character.projectiles.ProjectileFactory;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import sketchwars.game.World;

public class WeaponFactory
{
    public static final float GRENADE_SCALE = 0.04f;
    public static final float MELEE_SCALE = 0.052f;
    public static final float RIFLE_SCALE = 0.1f;
    public static final float ERASER_SCALE = 0.1f;
    
    public static HashMap<WeaponTypes, AbstractWeapon> createDefaultWeaponSet(ProjectileFactory fact) 
    {
        HashMap<WeaponTypes, AbstractWeapon> ret = new HashMap<>();
        ret.put(WeaponTypes.BASIC_GRENADE, createGrenade(fact));
        ret.put(WeaponTypes.MELEE_WEAPON, createBoxingGlove(fact));
        ret.put(WeaponTypes.RANGED_WEAPON, createRifle(fact));
        ret.put(WeaponTypes.ERASER, createEraser(fact));
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

    private static AbstractWeapon createEraser(ProjectileFactory fact) {
        Texture texture = Texture.loadTexture("content/char/weapons/pencileraser.png", true);
        Texture eraserImgTex = Texture.loadTexture("content/char/weapons/pencileraserArea.png", true);
        BufferedImage eraserImage = null;
        try {
            eraserImage = Texture.loadImageFile("content/char/weapons/pencileraserArea.png");
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        
        float ratio = texture.getTextureHeight()/texture.getTextureWidth();
        float width = RIFLE_SCALE;
        float height = width * ratio;
        
        EraserWeapon eraser = new EraserWeapon(texture, eraserImgTex, eraserImage, width, height, fact);
        eraser.setAmmo(AbstractWeapon.INFINITE_AMMO);
        
        return eraser;
    }

    private WeaponFactory() {}
}
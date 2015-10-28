package sketchwars.character.weapon;

import java.awt.image.BufferedImage;
import java.io.IOException;
import sketchwars.graphics.*;
import sketchwars.character.projectiles.ProjectileFactory;

import java.util.HashMap;
import sketchwars.OpenGL;
import sketchwars.game.SketchWarsWorld;

public class WeaponFactory
{
    public static final float GRENADE_SCALE = 0.04f;
    public static final float MELEE_SCALE = 0.052f;
    public static final float RIFLE_SCALE = 0.1f;
    public static final float ERASER_SCALE = 0.1f;
    public static final float PENCIL_SCALE = 0.1f;
    
    public static HashMap<WeaponTypes, AbstractWeapon> createDefaultWeaponSet(ProjectileFactory fact, SketchWarsWorld world) 
    {
        HashMap<WeaponTypes, AbstractWeapon> ret = new HashMap<>();
        ret.put(WeaponTypes.BASIC_GRENADE, createGrenade(fact));
        ret.put(WeaponTypes.MELEE_WEAPON, createBoxingGlove(fact));
        ret.put(WeaponTypes.RANGED_WEAPON, createRifle(fact));
        ret.put(WeaponTypes.ERASER, createEraser(fact, world));
        ret.put(WeaponTypes.PENCIL, createPencil(fact, world));
        return ret;
    }

    public static AbstractWeapon createGrenade(ProjectileFactory fact)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/grenade.png", false);
        
        float ratio = texture.getTextureHeight()/texture.getTextureWidth();
        float screenAspectRatio = OpenGL.getAspectRatio();
        float width = GRENADE_SCALE;
        float height = width * ratio * screenAspectRatio;
        
        AbstractWeapon weapon = new GrenadeWeapon(texture, width, height, fact);
        weapon.setAmmo(AbstractWeapon.INFINITE_AMMO);
        return weapon;
    }

    public static AbstractWeapon createBoxingGlove(ProjectileFactory fact)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/meleeBoxing.png", false);
        
        float ratio = texture.getTextureHeight()/texture.getTextureWidth();
        float screenAspectRatio = OpenGL.getAspectRatio();
        float width = MELEE_SCALE;
        float height = width * ratio * screenAspectRatio;
        
        AbstractWeapon weapon = new MeleeWeapon(texture, width, height, fact);
        weapon.setAmmo(AbstractWeapon.INFINITE_AMMO);
        return weapon;
    }

    public static AbstractWeapon createRifle(ProjectileFactory fact)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/rifle1.png", false);
        
        float ratio = texture.getTextureHeight()/texture.getTextureWidth();
        float screenAspectRatio = OpenGL.getAspectRatio();
        float width = RIFLE_SCALE;
        float height = width * ratio * screenAspectRatio;
        
        AbstractWeapon weapon = new RangedWeapon(texture, width, height, fact);
        weapon.setAmmo(AbstractWeapon.INFINITE_AMMO);
        return weapon;
    }

    private static AbstractWeapon createEraser(ProjectileFactory fact, SketchWarsWorld world) {
        Texture texture = Texture.loadTexture("content/char/weapons/pencileraser.png", false);
        Texture eraserImgTex = Texture.loadTexture("content/char/weapons/pencileraserArea.png", false);
        BufferedImage eraserImage = null;
        try {
            eraserImage = Texture.loadImageFile("content/char/weapons/pencileraserArea.png");
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        
        float ratio = texture.getTextureHeight()/texture.getTextureWidth();
        float screenAspectRatio = OpenGL.getAspectRatio();
        float width = ERASER_SCALE;
        float height = width * ratio * screenAspectRatio;
        
        PencilWeapon eraser = new PencilWeapon(texture, eraserImgTex, eraserImage, width, height, fact, true);
        eraser.setAmmo(AbstractWeapon.INFINITE_AMMO);
        eraser.setMap(world.getMap());
        
        return eraser;
    }

    private static AbstractWeapon createPencil(ProjectileFactory fact, SketchWarsWorld world) {
        Texture texture = Texture.loadTexture("content/char/weapons/pencil.png", false);
        Texture pencilImgTex = Texture.loadTexture("content/char/weapons/pencilpointArea.png", false);
        BufferedImage pencilpointImage = null;
        try {
            pencilpointImage = Texture.loadImageFile("content/char/weapons/pencilpointArea.png");
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        
        float ratio = texture.getTextureHeight()/texture.getTextureWidth();
        float screenAspectRatio = OpenGL.getAspectRatio();
        float width = PENCIL_SCALE;
        float height = width * ratio * screenAspectRatio;
        
        PencilWeapon pencil = new PencilWeapon(texture, pencilImgTex, pencilpointImage, width, height, fact, false);
        pencil.setAmmo(AbstractWeapon.INFINITE_AMMO);
        pencil.setMap(world.getMap());
        return pencil;
    }

    private WeaponFactory() {}
}
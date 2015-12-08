package sketchwars.character.weapon;

import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.IOException;
import sketchwars.graphics.*;
import sketchwars.character.projectiles.ProjectileFactory;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import sketchwars.OpenGL;
import sketchwars.game.SketchWarsWorld;

public class WeaponFactory
{
    public static final float GRENADE_SCALE = 0.04f;
    public static final float MELEE_SCALE = 0.052f;
    public static final float RIFLE_SCALE = 0.1f;
    public static final float ERASER_SCALE = 0.1f;
    public static final float PENCIL_SCALE = 0.1f;
    public static final float MINE_SCALE = 0.04f;
    public static final float CLUSTER_BOMB_SCALE = 0.04f;
    public static final float BAZOOKA_SCALE = 0.12f;
    
    public static HashMap<WeaponTypes, AbstractWeapon> createWeaponSet(ProjectileFactory projectileFactory, SketchWarsWorld world, WeaponSetTypes weaponSetSelected) 
    {
        switch(weaponSetSelected)
        {
        case MIX:
            return createDefaultWeaponSet(projectileFactory, world);
        case MELEE:
            return createMeleeWeaponSet(projectileFactory, world);
        case RANGE:
            return createRangedWeaponSet(projectileFactory, world);
        case EXPLOSIVE:
            return createExplosiveWeaponSet(projectileFactory, world);
        default:
            return new HashMap<>();
        }
    }
       
    private static HashMap<WeaponTypes, AbstractWeapon> createDefaultWeaponSet(ProjectileFactory fact, SketchWarsWorld world) 
    {
        HashMap<WeaponTypes, AbstractWeapon> ret = new HashMap<>();
        ret.put(WeaponTypes.BASIC_GRENADE, createGrenade(fact));
        ret.put(WeaponTypes.MELEE_WEAPON, createBoxingGlove(fact));
        ret.put(WeaponTypes.RANGED_WEAPON, createRifle(fact));
        ret.put(WeaponTypes.ERASER, createEraser(fact, world));
        ret.put(WeaponTypes.PENCIL, createPencil(fact, world));
        ret.put(WeaponTypes.MINE, createMine(fact));
        ret.put(WeaponTypes.CLUSTER_BOMB, createClusterBomb(fact));
        ret.put(WeaponTypes.BAZOOKA, createBazooka(fact));
        return ret;
    }
    
    private static HashMap<WeaponTypes, AbstractWeapon> createMeleeWeaponSet(ProjectileFactory fact, SketchWarsWorld world) 
    {
        HashMap<WeaponTypes, AbstractWeapon> ret = new HashMap<>();
        ret.put(WeaponTypes.MELEE_WEAPON, createBoxingGlove(fact));
        ret.put(WeaponTypes.ERASER, createEraser(fact, world));
        ret.put(WeaponTypes.PENCIL, createPencil(fact, world));
        ret.put(WeaponTypes.MINE, createMine(fact));
        return ret;
    }
    
    private static HashMap<WeaponTypes, AbstractWeapon> createRangedWeaponSet(ProjectileFactory fact, SketchWarsWorld world) 
    {
        HashMap<WeaponTypes, AbstractWeapon> ret = new HashMap<>();
        ret.put(WeaponTypes.RANGED_WEAPON, createRifle(fact));
        ret.put(WeaponTypes.BAZOOKA, createBazooka(fact));
        return ret;
    }
    
    private static HashMap<WeaponTypes, AbstractWeapon> createExplosiveWeaponSet(ProjectileFactory fact, SketchWarsWorld world) 
    {
        HashMap<WeaponTypes, AbstractWeapon> ret = new HashMap<>();
        ret.put(WeaponTypes.BAZOOKA, createBazooka(fact));
        ret.put(WeaponTypes.BASIC_GRENADE, createGrenade(fact));
        ret.put(WeaponTypes.CLUSTER_BOMB, createClusterBomb(fact));
        ret.put(WeaponTypes.MINE, createMine(fact));
        return ret;
    }

    public static AbstractWeapon createGrenade(ProjectileFactory fact)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/grenade.png", false);
        
        float ratio = texture.getTextureHeight()/texture.getTextureWidth();
        float width = GRENADE_SCALE;
        float height = width * ratio;
        
        AbstractWeapon weapon = new GrenadeWeapon(texture, width, height, fact);
        weapon.setAmmo(AbstractWeapon.INFINITE_AMMO);
        return weapon;
    }

    public static AbstractWeapon createBoxingGlove(ProjectileFactory fact)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/meleeBoxing.png", false);
        
        float ratio = texture.getTextureHeight()/texture.getTextureWidth();;
        float width = MELEE_SCALE;
        float height = width * ratio;
        
        AbstractWeapon weapon = new MeleeWeapon(texture, width, height, fact);
        weapon.setAmmo(AbstractWeapon.INFINITE_AMMO);
        return weapon;
    }

    public static AbstractWeapon createRifle(ProjectileFactory fact)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/rifle1.png", false);
        
        float ratio = texture.getTextureHeight()/texture.getTextureWidth();
        float width = RIFLE_SCALE;
        float height = width * ratio;
        
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
            System.out.println(ex.getMessage());
        }
        
        float ratio = texture.getTextureHeight()/texture.getTextureWidth();
        float width = ERASER_SCALE;
        float height = width * ratio;
        
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
            pencilpointImage = Texture.loadImageFile("content/char/weapons/pencileraserArea.png");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        
        float ratio = texture.getTextureHeight()/texture.getTextureWidth();
        float width = PENCIL_SCALE;
        float height = width * ratio;
        
        PencilWeapon pencil = new PencilWeapon(texture, pencilImgTex, pencilpointImage, width, height, fact, false);
        pencil.setAmmo(AbstractWeapon.INFINITE_AMMO);
        pencil.setMap(world.getMap());
        return pencil;
    }

    
    private static AbstractWeapon createMine(ProjectileFactory fact) {
        Texture texture = Texture.loadTexture("content/char/weapons/mine.png", false);
        
        float ratio = texture.getTextureHeight()/texture.getTextureWidth();
        float width = MINE_SCALE;
        float height = width * ratio;
        
        AbstractWeapon weapon = new MineWeapon(texture, width, height, fact);
        weapon.setAmmo(AbstractWeapon.INFINITE_AMMO);
        return weapon;
    }

    private static AbstractWeapon createClusterBomb(ProjectileFactory fact) {
        Texture texture = Texture.loadTexture("content/char/weapons/clusterBomb.png", false);
        
        float ratio = texture.getTextureHeight()/texture.getTextureWidth();
        float width = CLUSTER_BOMB_SCALE;
        float height = width * ratio;
        
        AbstractWeapon weapon = new ClusterBombWeapon(texture, width, height, fact);
        weapon.setAmmo(AbstractWeapon.INFINITE_AMMO);
        return weapon;
    }

    private static AbstractWeapon createBazooka(ProjectileFactory fact) {
        Texture texture = Texture.loadTexture("content/char/weapons/bazooka.png", false);
        
        float ratio = texture.getTextureHeight()/texture.getTextureWidth();
        float width = BAZOOKA_SCALE;
        float height = width * ratio;
        
        AbstractWeapon weapon = new BazookaWeapon(texture, width, height, fact);
        weapon.setAmmo(AbstractWeapon.INFINITE_AMMO);
        return weapon;
    }

 
}
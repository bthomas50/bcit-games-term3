package sketchwars.character.projectiles;

import java.awt.image.BufferedImage;
import sketchwars.game.*;
import sketchwars.physics.*;
import sketchwars.graphics.*;
import sketchwars.scenes.*;
import sketchwars.character.SketchCharacter;
import sketchwars.character.weapon.WeaponFactory;
import sketchwars.animation.*;
import sketchwars.exceptions.*;

import org.joml.Vector2d;
import sketchwars.map.AbstractMap;

public class ProjectileFactory
{
    private SketchWarsWorld world;
    private Physics physics;
    private Layer projectileLayer;

    public ProjectileFactory(SketchWarsWorld world, Physics physics, Scene<GameLayers> scene) throws SceneException
    {
        this.world = world;
        this.physics = physics;
        this.projectileLayer = scene.getLayer(GameLayers.PROJECTILE);
    }

    public AbstractProjectile createGrenade(SketchCharacter owner, long vPosition, long vVelocity)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/grenade.png", true);
        GrenadeProjectile proj = new GrenadeProjectile(texture, this);
        
        double ratio = texture.getTextureHeight()/texture.getTextureWidth();
        int widthP = (int)(WeaponFactory.GRENADE_SCALE * 1024.0f) ;
        int heightP = (int)(widthP * ratio) ;
        Collider coll = new GamePixelCollider(proj, BitMaskFactory.createRectangle(widthP, heightP));
        
        proj.setCollider(coll);

        setColliderProperties(coll, vPosition, vVelocity, 1.0f, 0.2f);

        addProjectile(proj);
        
        return proj;
    }

    public AbstractProjectile createMelee(SketchCharacter owner, long vPosition, long vVelocity)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/meleeBoxing.png", true);
        MeleeProjectile proj = new MeleeProjectile(texture, owner);
        double ratio = texture.getTextureHeight()/texture.getTextureWidth();
        int widthP = (int)(WeaponFactory.MELEE_SCALE * 1024.0f) ;
        int heightP = (int)(widthP * ratio) ;
        
        Collider coll = new GamePixelCollider(proj, BitMaskFactory.createRectangle(widthP, heightP));
        proj.setCollider(coll);

        setColliderProperties(coll, vPosition, vVelocity, 0.0f, 1.0f);

        addProjectile(proj);
        return proj;
    }

    public AbstractProjectile createRanged(SketchCharacter owner, long vPosition, long vVelocity)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/bullet1.png", true);
        RangedProjectile proj = new RangedProjectile(texture, owner);
        BitMask bm = BitMaskFactory.createLine(vPosition, vVelocity, RangedProjectile.RANGE);
        bm.trim();
        Collider coll = new GamePixelCollider(proj, bm);
        proj.setCollider(coll);
        
        setColliderProperties(coll, vPosition, vVelocity, 0.1f, 0.5f);

        addProjectile(proj);
        return proj;
    }

    public AnimatedProjectile createExplosion(long vPosition, double radius, int damage, BufferedImage explosionAlpha) {
        try {
            Explosion explosion = new Explosion();
            float posX = (float)Vectors.xComp(vPosition)/1024.0f;
            float posY = (float)Vectors.yComp(vPosition)/1024.0f;
            float width = (float) (radius / 1024.0f);
            float height = (float) ((radius / 1024.0f) * 1.4);
            
            explosion.setPosition(new Vector2d(posX, posY));
            explosion.setDimension(new Vector2d(width, height));
            
            AnimatedProjectile proj = new AnimatedProjectile(explosion, damage);
            Collider coll = new GamePixelCollider(proj, BitMaskFactory.createCircle(radius));
            proj.setCollider(coll);
            coll.setPosition(vPosition);
            world.addGameObject(proj);
            physics.addCollider(coll);
            projectileLayer.addDrawableObject(proj);
            
            //destroy terrain
            AbstractMap map = world.getMap();
            float x = posX - width/2.2f;
            float y = posY + height/2.0f;
            if (map.updateTexture(explosionAlpha, true, x, y, width, height)) {
                map.updateInPhysics(explosionAlpha, true, x + 0.01f, y - height + 0.05f, width, height);
            }
            
            return proj;
        } catch (AnimationException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

    protected void setColliderProperties(Collider coll, long vPosition, long vVelocity, float mass, float elasticity)
    {
        coll.setPosition(vPosition);
        coll.setVelocity(vVelocity);
        coll.setMass(mass);
        coll.setElasticity(elasticity);
    }

    private void addProjectile(AbstractProjectile proj) 
    {
        world.addGameObject(proj);
        physics.addCollider(proj.getCollider());
        projectileLayer.addDrawableObject(proj); //so it can be rendered
    }


}
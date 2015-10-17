package sketchwars.character.projectiles;

import sketchwars.game.*;
import sketchwars.physics.*;
import sketchwars.graphics.*;
import sketchwars.scenes.*;
import sketchwars.character.SketchCharacter;
import sketchwars.animation.*;
import sketchwars.util.CoordinateSystem;
import sketchwars.exceptions.*;

import org.joml.Vector2d;

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

    public AbstractProjectile createGrenade(SketchCharacter owner, long vPosition, long vVelocity, double scale)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/grenade.png");
        GrenadeProjectile proj = new GrenadeProjectile(texture);
        Collider coll = new GamePixelCollider(proj, BitMaskFactory.createCircle(GrenadeProjectile.COLLIDER_RADIUS));
        proj.setCollider(coll);

        setColliderProperties(coll, vPosition, vVelocity, 1.0f, 0.9f);

        addProjectile(proj);
        
        return proj;
    }

    public AbstractProjectile createMelee(SketchCharacter owner, long vPosition, long vVelocity, double scale)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/meleeBoxing.png");
        MeleeProjectile proj = new MeleeProjectile(texture, owner);
        Collider coll = new GamePixelCollider(proj, BitMaskFactory.createCircle(MeleeProjectile.COLLIDER_RADIUS));
        proj.setCollider(coll);

        setColliderProperties(coll, vPosition, vVelocity, 0.0f, 1.0f);

        addProjectile(proj);
        return proj;
    }

    public AbstractProjectile createRanged(SketchCharacter owner, long vPosition, long vVelocity, double scale)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/bullet1.png");
        RangedProjectile proj = new RangedProjectile(texture, owner);
        BitMask bm = BitMaskFactory.createLine(vPosition, vVelocity, RangedProjectile.RANGE);
        bm.trim();
        Collider coll = new GamePixelCollider(proj, bm);
        proj.setCollider(coll);
        
        setColliderProperties(coll, vPosition, vVelocity, 0.1f, 0.5f);

        addProjectile(proj);
        return proj;
    }

    public AnimatedProjectile createExplosion(AbstractProjectile bp, double radius) {
        try {
            long explosionPoint = bp.getCollider().getPosition();
            Explosion explosion = new Explosion();
            explosion.setPosition(CoordinateSystem.physicsToOpenGL(explosionPoint));
            
            explosion.setDimension(new Vector2d(radius, radius));
            AnimatedProjectile proj = new AnimatedProjectile(explosion);
            Collider coll = new GamePixelCollider(proj, BitMaskFactory.createCircle(radius));
            proj.setCollider(coll);
            coll.setPosition(bp.getCollider().getPosition());
            projectileLayer.addAnimation(explosion);
            explosion.start();
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
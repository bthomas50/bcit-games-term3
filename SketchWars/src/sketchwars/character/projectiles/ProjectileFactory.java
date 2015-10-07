package sketchwars.character.projectiles;

import sketchwars.*;
import sketchwars.physics.*;
import sketchwars.graphics.*;
import sketchwars.scenes.*;

public class ProjectileFactory
{
    private World world;
    private Physics physics;
    private AbstractScene scene;

    public ProjectileFactory(World world, Physics physics, AbstractScene scene)
    {
        this.world = world;
        this.physics = physics;
        this.scene = scene;
    }

    public BasicProjectile createGrenade(long vPosition, long vVelocity, double scale)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/grenade.png");
        BasicProjectile proj = new GrenadeProjectile(texture);
        Collider coll = new PixelCollider(BitMaskFactory.createCircle(32.0));
        proj.setCollider(coll);

        setColliderProperties(coll, vPosition, vVelocity, 1.0f, 0.9f);

        world.addGameObject(proj);
        physics.addCollider(coll);
        scene.addDrawableObject(proj);
        return proj;
    }

    public BasicProjectile createMelee(long vPosition, long vVelocity, double scale)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/meleeBoxing.png");
        BasicProjectile proj = new MeleeProjectile(texture);
        Collider coll = new PixelCollider(BitMaskFactory.createCircle(48.0));
        proj.setCollider(coll);

        setColliderProperties(coll, vPosition, vVelocity, 0.0f, 1.0f);

        world.addGameObject(proj);
        physics.addCollider(coll);
        scene.addDrawableObject(proj);
        return proj;
    }

    public BasicProjectile createRanged(long vPosition, long vVelocity, double scale)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/bullet1.png");
        BasicProjectile proj = new RangedProjectile(texture);
        Collider coll = new PixelCollider(BitMaskFactory.createCircle(4.0));
        proj.setCollider(coll);

        setColliderProperties(coll, vPosition, vVelocity, 0.1f, 0.5f);

        world.addGameObject(proj);
        physics.addCollider(coll);
        scene.addDrawableObject(proj);
        return proj;
    }

    protected void setColliderProperties(Collider coll, long vPosition, long vVelocity, float mass, float elasticity)
    {
        coll.setPosition(vPosition);
        coll.setVelocity(vVelocity);
        coll.setMass(mass);
        coll.setElasticity(elasticity);
    }

}
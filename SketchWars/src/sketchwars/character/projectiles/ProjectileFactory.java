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
        GrenadeProjectile proj = new GrenadeProjectile(texture);
        Collider coll = new PixelCollider(BitMaskFactory.createCircle(proj.getColliderRadius()));
        proj.setCollider(coll);

        setColliderProperties(coll, vPosition, vVelocity, 1.0f, 0.9f);

        addProjectile(proj);
        
        return proj;
    }

    public BasicProjectile createMelee(long vPosition, long vVelocity, double scale)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/meleeBoxing.png");
        MeleeProjectile proj = new MeleeProjectile(texture);
        Collider coll = new PixelCollider(BitMaskFactory.createCircle(proj.getMeleeObjRadius()));
        proj.setCollider(coll);

        setColliderProperties(coll, vPosition, vVelocity, 0.0f, 1.0f);

        addProjectile(proj);
        return proj;
    }

    public BasicProjectile createRanged(long vPosition, long vVelocity, double scale)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/bullet1.png");
        RangedProjectile proj = new RangedProjectile(texture);
        Collider coll = new PixelCollider(BitMaskFactory.createLine(vPosition, vVelocity, proj.getProjectileRange()));
        proj.setCollider(coll);
        System.out.println();
        setColliderProperties(coll, vPosition, vVelocity, 0.1f, 0.5f);

        addProjectile(proj);
        return proj;
    }

    protected void setColliderProperties(Collider coll, long vPosition, long vVelocity, float mass, float elasticity)
    {
        coll.setPosition(vPosition);
        coll.setVelocity(vVelocity);
        coll.setMass(mass);
        coll.setElasticity(elasticity);
    }

    private void addProjectile(BasicProjectile proj) {
        WeaponLogic weaponLogic = world.getWeaponLogic();
        
        if (weaponLogic != null) {
            weaponLogic.addProjectile(proj);
        } else {
            System.err.println("Weapon logic object given by the world class is a null pointer.");
        }
    }

}
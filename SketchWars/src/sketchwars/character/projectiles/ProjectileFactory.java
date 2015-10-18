package sketchwars.character.projectiles;

import sketchwars.game.*;
import sketchwars.physics.*;
import sketchwars.graphics.*;
import sketchwars.scenes.*;
import sketchwars.character.SketchCharacter;

public class ProjectileFactory
{
    private SketchWarsWorld world;
    private Physics physics;
    private Scene scene;

    public ProjectileFactory(SketchWarsWorld world, Physics physics, Scene scene)
    {
        this.world = world;
        this.physics = physics;
        this.scene = scene;
    }

    public BasicProjectile createGrenade(SketchCharacter owner, long vPosition, long vVelocity, double scale)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/grenade.png", false);
        GrenadeProjectile proj = new GrenadeProjectile(texture, scale);
        
        double width = texture.getTextureWidth() * scale;
        double height = texture.getTextureHeight() * scale;
        
        Collider coll = new PixelCollider(BitMaskFactory.createCircle(Math.max(width, height)));
        proj.setCollider(coll);

        setColliderProperties(coll, vPosition, vVelocity, 1.0f, 0.9f);

        proj.setOwner(owner);
        addProjectile(proj);
        
        return proj;
    }

    public BasicProjectile createMelee(SketchCharacter owner, long vPosition, long vVelocity, double scale)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/meleeBoxing.png", false);
        MeleeProjectile proj = new MeleeProjectile(texture, scale);
        double width = texture.getTextureWidth() * scale;
        double height = texture.getTextureHeight() * scale;
        Collider coll = new PixelCollider(BitMaskFactory.createCircle(Math.max(width, height)));
        proj.setCollider(coll);

        setColliderProperties(coll, vPosition, vVelocity, 0.0f, 1.0f);

        proj.setOwner(owner);
        addProjectile(proj);
        return proj;
    }

    public BasicProjectile createRanged(SketchCharacter owner, long vPosition, long vVelocity, double scale)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/bullet1.png", false);
        RangedProjectile proj = new RangedProjectile(texture, scale);
        BitMask bm = BitMaskFactory.createLine(vPosition, vVelocity, proj.getProjectileRange());
        bm.trim();
        System.out.println(bm.getBounds());
        Collider coll = new PixelCollider(bm);
        proj.setCollider(coll);
        
        setColliderProperties(coll, vPosition, vVelocity, 0.1f, 0.5f);

        proj.setOwner(owner);
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
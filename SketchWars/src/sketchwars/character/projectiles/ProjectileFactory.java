package sketchwars.character.projectiles;

import java.awt.image.BufferedImage;
import java.util.Random;
import org.joml.Matrix3d;
import sketchwars.game.*;
import sketchwars.physics.*;
import sketchwars.graphics.*;
import sketchwars.scenes.*;
import sketchwars.character.SketchCharacter;
import sketchwars.character.weapon.WeaponFactory;
import sketchwars.animation.*;
import sketchwars.exceptions.*;

import org.joml.Vector2d;
import org.joml.Vector3d;
import sketchwars.map.AbstractMap;
import sketchwars.util.Converter;

public class ProjectileFactory
{
    public static final float CLUSTER_SCALE = 0.02f;
    public static final float BAZOOKA_ROCKET_SCALE = 0.02f;
    
    private final SketchWarsWorld world;
    private final Physics physics;
    private final Layer projectileLayer;
    private Random rng;

    public ProjectileFactory(SketchWarsWorld world, Physics physics, Scene<GameLayers> scene, Random rng) throws SceneException
    {
        this.world = world;
        this.physics = physics;
        this.projectileLayer = scene.getLayer(GameLayers.PROJECTILE);
        this.rng = rng;
    }

    public AbstractProjectile createGrenade(SketchCharacter owner, long vPosition, long vVelocity)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/grenade.png", false);
        GrenadeProjectile proj = new GrenadeProjectile(texture, this);
        
        int widthP = Converter.GraphicsToPhysicsX(WeaponFactory.GRENADE_SCALE);
        int heightP = (int)(widthP) ;
        Collider coll = new GamePixelCollider(proj, BitMaskFactory.createRectangle(widthP, heightP));
        
        proj.setCollider(coll);

        setColliderProperties(coll, vPosition, vVelocity, 1.0f, 0.2f);

        addProjectile(proj);
        
        return proj;
    }

    public AbstractProjectile createMelee(SketchCharacter owner, long vPosition, long vVelocity)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/meleeBoxing.png", false);
        MeleeProjectile proj = new MeleeProjectile(texture, owner);
        int widthP = Converter.GraphicsToPhysicsX(WeaponFactory.MELEE_SCALE);
        int heightP = (int)(widthP);
        
        Collider coll = new GamePixelCollider(proj, BitMaskFactory.createRectangle(widthP, heightP), CollisionBehaviour.NOTIFY);
        proj.setCollider(coll);

        setColliderProperties(coll, vPosition, vVelocity, 0.0f, 1.0f);

        addProjectile(proj);
        return proj;
    }

    public AbstractProjectile createRanged(SketchCharacter owner, long vPosition, long vVelocity)
    {
        Texture texture = Texture.loadTexture("content/char/weapons/bullet1.png", false);
        RangedProjectile proj = new RangedProjectile(texture, owner);
        BitMask bm = BitMaskFactory.createLine(vPosition, vVelocity, RangedProjectile.RANGE);
        bm.trim();
        Collider coll = new GamePixelCollider(proj, bm, CollisionBehaviour.NOTIFY);
        proj.setCollider(coll);

        setColliderProperties(coll, vPosition, vVelocity, 1.0f, 0.01f);

        addProjectile(proj);
        return proj;
    }

    public AnimatedProjectile createExplosion(long vCenter, double radius, int damage, BufferedImage explosionAlpha) {
        try {
            Explosion explosion = new Explosion();
            float posX = Converter.PhysicsToGraphicsX(Vectors.xComp(vCenter));
            float posY = Converter.PhysicsToGraphicsY(Vectors.yComp(vCenter));
            
            float width = Converter.PhysicsToGraphicsX(radius * 2);
            float height = Converter.PhysicsToGraphicsY(radius * 2);
            
            explosion.setPosition(new Vector2d(posX, posY));
            explosion.setDimension(new Vector2d(width * 0.8f, height * 0.8f));
            
            AnimatedProjectile proj = new AnimatedProjectile(explosion, damage);
            Collider coll = new GamePixelCollider(proj, BitMaskFactory.createCircle(radius), CollisionBehaviour.NOTIFY);
            proj.setCollider(coll);
            coll.setPosition(Vectors.subtract(vCenter, Vectors.create(radius, radius)));
            world.addGameObject(proj);
            physics.addCollider(coll);
            projectileLayer.addDrawableObject(proj);
            
            //destroy terrain
            AbstractMap map = world.getMap();
            if (map.updateTexture(explosionAlpha, true, posX, posY, width, height)) {
                map.updateInPhysics(explosionAlpha, true, posX, posY, width, height);
            }
            
            return proj;
        } catch (AnimationException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

    protected void setColliderProperties(Collider coll, long vPosition, long vVelocity, float mass, float elasticity)
    {
        BoundingBox projBounds = coll.getBounds();
        long offset = Vectors.create(projBounds.getWidth()/2.0, (projBounds.getHeight()/2.0) + 50);
        long center = Vectors.add(vPosition, offset);
                
        coll.setPosition(center);
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

    public AbstractProjectile createMine(SketchCharacter owner, long vPosition, long vVelocity)
    {
        Texture activated = Texture.loadTexture("content/animation/weapons/mine.png", false);
        Animation acitvatedAnim = null;
        try {
            acitvatedAnim = new Animation(activated, 2, 2, 1, 1000, true);
            acitvatedAnim.start();
        } catch (AnimationException ex) {
            System.err.println(ex.getMessage());
        }
        
        Texture texture = Texture.loadTexture("content/char/weapons/mine.png", false);
        MineProjectile proj = new MineProjectile(texture, acitvatedAnim, this);
        
        int widthP = Converter.GraphicsToPhysicsX(WeaponFactory.MINE_SCALE) ;
        int heightP = (int)(widthP) ;
        Collider coll = new GamePixelCollider(proj, BitMaskFactory.createRectangle(widthP, heightP));
        
        proj.setCollider(coll);

        setColliderProperties(coll, vPosition, vVelocity, 5.0f, 0.2f);

        addProjectile(proj);
        
        return proj;
    }

    public AbstractProjectile createClusterBomb(SketchCharacter owner, long vPosition, long vVelocity) {
        Texture texture = Texture.loadTexture("content/char/weapons/clusterBomb.png", false);
        ClusterBombProjectile proj = new ClusterBombProjectile(texture, this);
        
        int widthP = Converter.GraphicsToPhysicsX(WeaponFactory.CLUSTER_BOMB_SCALE) ;
        int heightP = (int)(widthP) ;
        Collider coll = new GamePixelCollider(proj, BitMaskFactory.createRectangle(widthP, heightP));
        
        proj.setCollider(coll);

        setColliderProperties(coll, vPosition, vVelocity, 3.0f, 0.2f);

        addProjectile(proj);
        
        return proj;
    }

    public void createClusterMunitions(SketchCharacter owner, int power, long vPosition, int count) {
        Texture texture = Texture.loadTexture("content/char/weapons/clusterBomb.png", false);
        
        int widthP = Converter.GraphicsToPhysicsX(CLUSTER_SCALE) ;
        int heightP = (int)(widthP) ;
        
        float angle = (float)((Math.PI * 2)/(float)count);
        Vector3d velocity = new Vector3d((rng.nextFloat() * 2) - 1, rng.nextFloat() + 1, 0);
        Matrix3d matrix = new Matrix3d();
        matrix.rotate(angle, 0, 0, 1);
        
        for (int i = 0; i < count; i++) {
            ImpactProjectile proj = new ImpactProjectile(texture, this);
            Collider coll = new GamePixelCollider(proj, BitMaskFactory.createRectangle(widthP, heightP));
            proj.setCollider(coll);
                      
            matrix.transform(velocity);
            Vector2d vel = new Vector2d(velocity.x, velocity.y);
            vel.normalize();
           
            setColliderProperties(coll, vPosition, Vectors.create(vel.x * power, vel.y * power), 1.0f, 0.2f);

            addProjectile(proj);
        }
    }

    public AbstractProjectile createBazookaRocket(SketchCharacter owner, long vPosition, long vVelocity) {
        Texture activated = Texture.loadTexture("content/animation/weapons/flameSpriteSheet.png", false);
        Animation flameAnim = null;
        try {
            flameAnim = new Animation(activated, 9, 3, 3, 200, true);
            flameAnim.start();
        } catch (AnimationException ex) {
            System.err.println(ex.getMessage());
        }
        
        Texture texture = Texture.loadTexture("content/char/weapons/bazookaRocket.png", false);
        BazookaProjectile proj = new BazookaProjectile(texture, flameAnim, this);
        
        int widthP = Converter.GraphicsToPhysicsX(BAZOOKA_ROCKET_SCALE) ;
        int heightP = (int)(widthP) ;
        Collider coll = new GamePixelCollider(proj, BitMaskFactory.createRectangle(widthP, heightP));
        
        proj.setCollider(coll);

        double offset = Converter.GraphicsToPhysicsX(0.01f);
        long distance = Vectors.scalarMultiply(offset, Vectors.normalize(vVelocity));
        
        setColliderProperties(coll, Vectors.add(vPosition, distance), vVelocity, 0.5f, 0.2f);

        addProjectile(proj);
        
        return proj;
    }
}
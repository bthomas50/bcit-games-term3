package sketchwars.game;

import sketchwars.animation.Explosion;
import java.util.ArrayList;
import org.joml.Vector2d;
import sketchwars.character.projectiles.BasicProjectile;
import sketchwars.physics.Physics;
import sketchwars.character.SketchCharacter;
import sketchwars.character.projectiles.GrenadeProjectile;
import sketchwars.exceptions.AnimationException;
import sketchwars.exceptions.SceneException;
import sketchwars.physics.BitMaskFactory;
import sketchwars.physics.Collider;
import sketchwars.physics.Collisions;
import sketchwars.physics.PixelCollider;
import sketchwars.physics.Vectors;
import sketchwars.scenes.Scene;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class WeaponLogic implements GameObject {
    
    private final ArrayList<BasicProjectile> projectiles;
    private final SketchWarsWorld world;
    private final Physics physics;
    private final Scene<GameLayers> gameScene;
        
    public WeaponLogic(SketchWarsWorld world, Scene<GameLayers> scene, Physics physics) {
        this.world = world;
        this.physics = physics;
        this.gameScene = scene;
        projectiles = new ArrayList<>();
    }

    @Override
    public void update(double delta) {
        updateProjectiles(delta);
        updateDamageGiven(delta);
        removeExpiredProjectiles(delta);
    }
    
    public void addProjectile(BasicProjectile projectile) {
        try {
            projectiles.add(projectile);
            physics.addCollider(projectile.getCollider());
            projectile.setActive(true);
            
            gameScene.getLayer(GameLayers.PROJECTILE).addDrawableObject(projectile); //so it can be rendered
        } catch (SceneException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void updateProjectiles(double delta) {
        for (BasicProjectile projectile: projectiles) {
            projectile.update(delta);
        }
    }

    private void updateDamageGiven(double delta) {
        ArrayList<SketchCharacter> characters = world.characters;
        ArrayList<BasicProjectile> consumedProjectiles = new ArrayList<>();
        
        for (BasicProjectile projectile: projectiles) {
            for (SketchCharacter character: characters) {
                
                if (hasProjectileHitTarget(character, projectile)) {
                    character.takeDamage(projectile.getDamage());
                    
                    if (consumedProjectiles.contains(projectile)) {
                        consumedProjectiles.add(projectile);
                    }
                    
                    System.out.println(character + " is hit for " + projectile.getDamage() + " damage.");
                }
            }
        }
        
        for (BasicProjectile projectile: consumedProjectiles) {
            projectile.setConsumed(true);
        }
    }

    private boolean hasProjectileHitTarget(SketchCharacter character, BasicProjectile projectile) {
        Collider charCollider = character.getCollider();
        
        if (projectile instanceof GrenadeProjectile) { //handle grenades separately
            GrenadeProjectile grenade = (GrenadeProjectile)projectile;
            if (projectile.hasExpired() || projectile.isConsumed()) {
                return hasGrenadeHitTarget(charCollider, grenade);
            }
        } else if (!projectile.getOwner().equals(character)) {
            Collider projectileCollider = projectile.getCollider();
            return (Collisions.hasCollided(charCollider, projectileCollider));
        }
        
        return false;
    }
    
    private boolean hasGrenadeHitTarget(Collider characterCollider, GrenadeProjectile grenade) {
        Collider grenadeCollider = grenade.getCollider();
        Collider explosionCollider = new PixelCollider(BitMaskFactory.createCircle(grenade.getExplosionRadius() * 1024.0f));
        
        explosionCollider.setPosition(grenadeCollider.getPosition());
        
        return (Collisions.hasCollided(characterCollider, explosionCollider));
    }

    private void createExplosionObject(BasicProjectile bp, double radius) {
        try {
            long explosionPoint = bp.getCollider().getPosition();
            Explosion explosion = new Explosion();
            explosion.start();
            
            float posX = (float)Vectors.xComp(explosionPoint)/1024.0f;
            float posY = (float)Vectors.yComp(explosionPoint)/1024.0f;
            explosion.setPosition(new Vector2d(posX, posY));
            
            explosion.setDimension(new Vector2d(radius, radius * 1.3));
            gameScene.getLayer(GameLayers.PROJECTILE).addAnimation(explosion);
        } catch (AnimationException | SceneException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    private void removeExpiredProjectiles(double delta) {
        int size = projectiles.size();
        
        for (int i = size - 1; i >= 0; i--) {
            BasicProjectile bp = projectiles.get(i);
            
            if (bp.hasExpired()) {
                if (bp instanceof GrenadeProjectile) {
                    GrenadeProjectile grenade = (GrenadeProjectile)bp;
                    createExplosionObject(grenade, grenade.getExplosionRadius());
                }
                
                physics.removeCollider(bp.getCollider());
                projectiles.remove(i);
                
                try {
                    gameScene.getLayer(GameLayers.PROJECTILE).removeDrawableObject(bp);
                } catch (SceneException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
    }
}

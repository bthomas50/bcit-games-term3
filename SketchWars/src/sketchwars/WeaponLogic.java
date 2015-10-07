/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars;

import sketchwars.animation.Explosion;
import java.util.ArrayList;
import sketchwars.character.projectiles.BasicProjectile;
import sketchwars.graphics.GraphicsObject;
import sketchwars.physics.Physics;
import sketchwars.character.Character;
import sketchwars.character.projectiles.GrenadeProjectile;
import sketchwars.physics.BitMask;
import sketchwars.physics.BitMaskFactory;
import sketchwars.physics.Collider;
import sketchwars.physics.Collisions;
import sketchwars.physics.PixelCollider;
import sketchwars.physics.Vectors;
import sketchwars.scenes.GameScene;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class WeaponLogic implements GameObject, GraphicsObject {
    private final ArrayList<BasicProjectile> projectiles;
    private final World world;
    private final Physics physics;
    private final GameScene scene;
        
    public WeaponLogic(World world, GameScene scene, Physics physics) {
        this.world = world;
        this.physics = physics;
        this.scene = scene;
        projectiles = new ArrayList<>();
    }

    @Override
    public void update(double delta) {
        updateProjectiles(delta);
        updateDamageGiven(delta);
        removeExpiredProjectiles(delta);
    }

    @Override
    public void render() {
        for (BasicProjectile projectile: projectiles) {
            projectile.render();
        }
    }
    
    public void addProjectile(BasicProjectile projectile) {
        projectiles.add(projectile);
        physics.addCollider(projectile.getCollider());
        projectile.setActive(true);
    }

    private void updateProjectiles(double delta) {
        for (BasicProjectile projectile: projectiles) {
            projectile.update(delta);
        }
    }

    private void updateDamageGiven(double delta) {
        ArrayList<Character> characters = world.characters;
        ArrayList<BasicProjectile> consumedProjectiles = new ArrayList<>();
        
        for (BasicProjectile projectile: projectiles) {
            for (Character character: characters) {
                
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

    private boolean hasProjectileHitTarget(Character character, BasicProjectile projectile) {
        Collider charCollider = character.getCollider();
        
        if (projectile instanceof GrenadeProjectile) { //handle grenades separately
            GrenadeProjectile grenade = (GrenadeProjectile)projectile;
            if (projectile.hasExpired() || projectile.isConsumed()) {
                createExplosionObject(grenade, grenade.getExplosionRadius());
                return hasGrenadeHitTarget(charCollider, grenade);
            }
        } else {
            Collider projectileCollider = projectile.getCollider();
            return (Collisions.hasCollided(charCollider, projectileCollider));
        }
        
        return false;
    }
    
    private boolean hasGrenadeHitTarget(Collider characterCollider, GrenadeProjectile grenade) {
        Collider grenadeCollider = grenade.getCollider();
        Collider explosionCollider = new PixelCollider(BitMaskFactory.createCircle(grenade.getExplosionRadius()));
        
        explosionCollider.setPosition(grenadeCollider.getPosition());
        
        return (Collisions.hasCollided(characterCollider, explosionCollider));
    }

    private void createExplosionObject(BasicProjectile bp, double radius) {
        long explosionPoint = bp.getCollider().getPosition();
        Explosion explosion = new Explosion();
        explosion.setPosition(explosionPoint);
        
        explosion.setDimension(Vectors.create(radius, radius));
        scene.addAnimation(explosion);
        explosion.start();
    }
    
    private void removeExpiredProjectiles(double delta) {
        int size = projectiles.size();
        
        for (int i = size - 1; i >= 0; i--) {
            BasicProjectile bp = projectiles.get(i);
            
            if (bp.hasExpired()) {
                physics.removePhysicsObject(bp.getCollider());
                projectiles.remove(i);
            }
        }
    }
}

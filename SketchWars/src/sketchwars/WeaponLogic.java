/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars;

import java.util.ArrayList;
import sketchwars.character.projectiles.BasicProjectile;
import sketchwars.graphics.GraphicsObject;
import sketchwars.physics.Physics;
import sketchwars.character.Character;
import sketchwars.character.projectiles.GrenadeProjectile;
import sketchwars.physics.Collider;
import sketchwars.physics.Collisions;
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
        
        for (BasicProjectile projectile: projectiles) {
            for (Character character: characters) {
                Collider charCollider = character.getCollider();
                Collider projectileCollider = projectile.getCollider();
                
                boolean hasCollided = Collisions.hasCollided(charCollider, projectileCollider);
                    System.out.println("----------------------------------------------------------------------" + hasCollided);
                
                if (hasCollided) {
                    character.takeDamage(projectile.getDamage());
                }
            }
        }
    }

    private void removeExpiredProjectiles(double delta) {
        int size = projectiles.size();
        
        for (int i = size - 1; i >= 0; i--) {
            BasicProjectile bp = projectiles.get(i);
            
            if (bp.hasExpired()) {
                physics.removePhysicsObject(bp.getCollider());
                projectiles.remove(i);
                
                if (bp instanceof GrenadeProjectile) {
                    createExplosionObject(bp); 
                }
            }
        }
    }

    private void createExplosionObject(BasicProjectile bp) {
        long explosionPoint = bp.getCollider().getPosition();
        Explosion explosion = new Explosion();
        explosion.setPosition(explosionPoint);
        explosion.setDimension(Vectors.create(bp.getCollider().getBounds().getWidth(), 
                bp.getCollider().getBounds().getHeight()));
        scene.addAnimation(explosion);
        explosion.start();
    }
}

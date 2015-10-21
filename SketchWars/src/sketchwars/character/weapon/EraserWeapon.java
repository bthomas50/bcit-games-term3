/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.character.weapon;

import sketchwars.character.SketchCharacter;
import sketchwars.character.projectiles.AbstractProjectile;
import sketchwars.character.projectiles.ProjectileFactory;
import sketchwars.graphics.Texture;
import sketchwars.input.MouseHandler;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class EraserWeapon extends AbstractWeapon {

    public EraserWeapon(Texture texture, float width, float height, ProjectileFactory projectileFactory) {
        super(texture, width, height, projectileFactory);
    }

    @Override
    public void render() {
        posX = MouseHandler.getNormalizedX();
        posY = MouseHandler.getNormalizedY();
        super.render(); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    protected AbstractProjectile createProjectile(SketchCharacter owner, long vPosition, long vVelocity) {
        return null;
    }

    @Override
    protected double getProjectileSpeed(float power) {
        return 0;
    }
    
}

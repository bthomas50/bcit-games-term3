/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.character.weapon;

import java.awt.image.BufferedImage;
import sketchwars.character.SketchCharacter;
import sketchwars.character.projectiles.AbstractProjectile;
import sketchwars.character.projectiles.ProjectileFactory;
import sketchwars.graphics.Texture;
import sketchwars.input.MouseHandler;
import sketchwars.input.MouseState;
import sketchwars.map.AbstractMap;
import sketchwars.physics.Vectors;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class PencilWeapon extends AbstractWeapon {
    private final Texture point;
    private final BufferedImage pointImage;
    private AbstractMap currentMap;
    
    private final float eWidth;
    private final float eHeight;
    
    private final boolean eraser;
    
    public PencilWeapon(Texture texture, Texture point, BufferedImage pointImage, float width, float height, 
            ProjectileFactory projectileFactory, boolean eraser) {
        super(texture, width, height, projectileFactory);
        
        this.eraser = eraser;
        this.point = point;
        this.pointImage = pointImage;
        
        eWidth = width/2;
        eHeight = height/2;
    }

    @Override
    public void update(double elapsed) {
        posX = MouseHandler.getNormalizedX();
        posY = MouseHandler.getNormalizedY();
        
        handleErasing();
        
        super.update(elapsed); 
    }
    
    @Override
    public void render() {
        if (texture != null) {
            texture.draw(null, posX, posY, width, height);
        }
    }

    public void setMap(AbstractMap map) {
        this.currentMap = map;
    }

    @Override
    protected AbstractProjectile createProjectile(SketchCharacter owner, long vPosition, long vVelocity) {
        return null;
    }

    @Override
    protected double getProjectileSpeed(float power) {
        return 0;
    }

    private void handleErasing() {
        if (point != null && currentMap != null && pointImage != null &&
                MouseHandler.state == MouseState.DOWN) {
            float xEraser = posX - 0.04f;
            float yEraser = posY - 0.08f;
            
            point.draw(null, xEraser, yEraser, eWidth, eHeight);
            
            if (currentMap.updateTexture(pointImage, eraser, xEraser, yEraser, eWidth, eHeight)) {
                currentMap.updateInPhysics(pointImage, eraser, xEraser, yEraser, eWidth, eHeight);
            }
        }
    }
}

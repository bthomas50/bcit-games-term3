/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.character.weapon;

import java.awt.image.BufferedImage;
import sketchwars.character.SketchCharacter;
import sketchwars.character.projectiles.*;
import sketchwars.graphics.Texture;
import sketchwars.input.*;
import sketchwars.map.AbstractMap;
import sketchwars.physics.Vectors;
import sketchwars.util.*;

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
    private static final int FIRING_TIME_MILLIS = 2000;
    
    private final boolean isEraser;

    private Timer timer;
    
    public PencilWeapon(Texture texture, Texture point, BufferedImage pointImage, float width, float height, 
            ProjectileFactory projectileFactory, boolean isEraser) {
        super(texture, width, height, projectileFactory);
        
        this.isEraser = isEraser;
        if(point == null || pointImage == null) {
            throw new IllegalArgumentException("point and pointImage must not be null");
        }
        this.point = point;
        this.pointImage = pointImage;
        
        eWidth = width/2;
        eHeight = height/2;

        timer = new Timer(FIRING_TIME_MILLIS);
    }

    @Override
    public void update(double elapsed) {
        posX = MouseHandler.xNormalized;
        posY = MouseHandler.yNormalized;
        timer.update(elapsed);
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
    public AbstractProjectile tryToFire(SketchCharacter owner, float power, long vAimDirection) {
        if(isFiringPossible()) {
            handleErasing();
        } else {
            owner.notifyFired();
        }
        return null;
    }

    @Override
    public void resetFire() {
        timer.reset();
    }

    @Override
    protected AbstractProjectile createProjectile(SketchCharacter owner, long vPosition, long vVelocity) {
        throw new UnsupportedOperationException("not defined for Pencil-Eraser");
    }

    @Override
    protected double getProjectileSpeed(float power) {
        throw new UnsupportedOperationException("not defined for Pencil-Eraser");
    }

    private void handleErasing() {
        if(!timer.isRunning()) {
            timer.restart();
        }
        float xEraser = posX - 0.04f;
        float yEraser = posY - 0.08f;
        
        point.draw(null, xEraser, yEraser, eWidth, eHeight);
        
        if (currentMap.updateTexture(pointImage, isEraser, xEraser, yEraser, eWidth, eHeight)) {
            currentMap.updateInPhysics(pointImage, isEraser, xEraser, yEraser, eWidth, eHeight);
        }
    }

    private boolean isFiringPossible() {
        return currentMap != null &&
               !timer.hasElapsed();
    }


}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.character.weapon;

import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import org.joml.Vector2d;
import sketchwars.character.SketchCharacter;
import sketchwars.character.projectiles.*;
import sketchwars.graphics.Texture;
import sketchwars.map.AbstractMap;
import sketchwars.physics.BitMaskFactory;
import sketchwars.physics.Collider;
import sketchwars.physics.Physics;
import sketchwars.physics.PixelCollider;
import sketchwars.physics.Vectors;
import sketchwars.ui.components.Label;
import sketchwars.util.*;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class PencilWeapon extends AbstractWeapon {
    private final Texture point;
    private final BufferedImage pointImage;
    private AbstractMap currentMap;
    
    //so that we can check if any characters are in the way of the cursor.
    private Physics physics;
    
    private final float eWidth;
    private final float eHeight;
    private static final int FIRING_TIME_MILLIS = 2000;
    
    private final boolean isEraser;

    private Timer timer;
    private float targetX;
    private float targetY;
    private Label timeLabel;
    NumberFormat formatter = new DecimalFormat("#0.0");
    
    public PencilWeapon(Texture texture, Texture point, BufferedImage pointImage, float width, float height, 
            ProjectileFactory projectileFactory, Physics phys, boolean isEraser) {
        super(texture, width, height, projectileFactory);
        this.physics = phys;
        this.isEraser = isEraser;
        if(point == null || pointImage == null) {
            throw new IllegalArgumentException("point and pointImage must not be null");
        }
        this.point = point;
        this.pointImage = pointImage;
        
        eWidth = width/2;
        eHeight = height/2;

        timer = new Timer(FIRING_TIME_MILLIS);
        targetX = 0;
        targetY = 0;
        
        timeLabel = new Label("", null, new Vector2d(targetX, targetY), new Vector2d(0.35,0.35), null);
    }

    @Override
    public void update(double elapsed) {
        timer.update(elapsed);
        timeLabel.setPosition(new Vector2d(targetX + 0.1f, targetY - 0.1f));
        timeLabel.setText(formatter.format(timer.getRemainingMillis()/1000));
        super.update(elapsed);
    }
    
    @Override
    public void render() {
        if (texture != null) {
            texture.draw(null, targetX + eWidth, targetY + eHeight, width, height);
        if (timer.getRemainingMillis() != 0)
            timeLabel.render();
        }
    }

    public void setMap(AbstractMap map) {
        this.currentMap = map;
    }

    @Override
    public void aimAt(float x, float y) {
        super.aimAt(x, y);
        targetX = x;
        targetY = y;
    }
    //disable keyboard firing for this weapon.
    @Override 
    public AbstractProjectile tryToFire(SketchCharacter owner, float power) {
        if(isFiringPossible()) {
            if(!areCharactersInTarget())
            {
                handleErasing();
            }
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
        
        point.draw(null, targetX, targetY, eWidth, eHeight);
        
        if (currentMap.updateTexture(pointImage, isEraser, targetX, targetY, eWidth, eHeight)) {
            currentMap.updateInPhysics(pointImage, isEraser, targetX, targetY, eWidth, eHeight);
        }
    }

    private boolean isFiringPossible() {
        return currentMap != null &&
               !timer.hasElapsed();
    }
    
    private boolean areCharactersInTarget() {
        int widthPhysics = Converter.GraphicsToPhysicsX(eWidth);
        int heightPhysics = Converter.GraphicsToPhysicsY(eHeight);
        
        int xPhysics = Converter.GraphicsToPhysicsX(targetX) - widthPhysics/2;
        int yPhysics = Converter.GraphicsToPhysicsY(targetY) - heightPhysics/2;
        Collider c = new PixelCollider(BitMaskFactory.createCircle(widthPhysics/2));
        c.setPosition(Vectors.create(xPhysics, yPhysics));
        System.out.println(widthPhysics + ", " + heightPhysics);
        System.out.println(xPhysics + ", " + yPhysics);
        List<Collider> lst = physics.getCollisions(c);
        for(Collider c2 : lst) 
        {
            System.out.println(c2);
            if(c2.hasAttachedGameObject() && 
               c2.getAttachedGameObject() instanceof SketchCharacter)
            {
                return true;
            }
        }
        return false;
    }


}

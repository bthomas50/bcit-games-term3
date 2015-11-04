/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.scenes;

import org.joml.Vector2d;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class Camera {
    private static final float MIN_DIST = 0.01f;
    private static final float PAN_SPEED = 0.0005f;
    
    private float panSpeed;
    
    private final float worldLeft;
    private final float worldRight;
    private final float worldTop;
    private final float worldBottom;

    private final float worldWidth;
    private final float worldHeight;
    
    private float left;
    private float right;
    private float top;
    private float bottom;
    
    private float nextLeft;
    private float nextRight;
    private float nextTop;
    private float nextBottom;
    
    private float width;
    private float height;
    
    private boolean startPanning;
    
    public Camera(float worldLeft, float worldTop, float worldWidth, float worldHeight) {
        this.worldLeft = worldLeft;
        this.worldRight = worldLeft + worldWidth;
        this.worldTop = worldTop;
        this.worldBottom = worldTop - worldHeight;
                
        panSpeed = PAN_SPEED;
        
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.width = this.worldWidth;
        this.height = this.worldHeight;
        
        this.left = this.worldLeft;
        this.right = this.worldRight;
        this.top = this.worldTop;
        this.bottom = this.worldBottom;
        
        this.nextLeft = this.left;
        this.nextRight = this.right;
        this.nextTop = this.top;
        this.nextBottom = this.bottom;
    }

    public void setPanning(boolean panning) {
        this.startPanning = panning;
    }

    public float getPanSpeed() {
        return panSpeed;
    }

    public void setPanSpeed(float panSpeed) {
        this.panSpeed = panSpeed;
    }

    
    public void setCameraSize(float width, float height) {
        this.width = width;
        this.height = height;
    }
    
    public void setCameraPosition(float xCenter, float yCenter) {
        float xOffset = width/2.0f;
        float yOffset = height/2.0f;
        
        left = xCenter - xOffset;
        right = xCenter + xOffset;
        top = yCenter + yOffset;
        bottom = yCenter - yOffset;
    }
    
    public void setNextCameraPosition(float xCenter, float yCenter) {
        float xOffset = width/2.f;
        float yOffset = height/2.f;
        
        nextLeft = Math.max(xCenter - xOffset, worldLeft);
        nextRight = Math.min(xCenter + xOffset, worldRight);
        nextTop = Math.min(yCenter + yOffset, worldTop);
        nextBottom = Math.max(yCenter - yOffset, worldBottom);
    
        float newWidth = nextRight - nextLeft;
        float newHeight = nextTop - nextBottom;
        if (newWidth < width) {
            float difference = width - newWidth;
            if (nextLeft == worldLeft) {
                nextRight += difference;
            } else {
                nextLeft -= difference;
            }
        }
        if (newHeight < height) {
            float difference = height - newHeight;
            if (nextTop == worldTop) {
                nextLeft += difference;
            } else {
                nextTop -= difference;
            }
        }
        
        //System.out.println("NextLeft: " + nextLeft + ", NextTop: " + nextTop +
        //                   ", NextRight: " + nextRight + ", NextBottom: " + nextBottom);
    }
    
    public void update(double delta) {
        if (startPanning) {
            handleCameraPan(delta);
        }
    }
    
    public void applyCameraSettings() {
        float xScale = worldWidth/width;
        float yScale = worldHeight/height;
        
        float xOffset = -(left + (width / xScale));
        float yOffset = -(top - (height / yScale));
        
        GL11.glLoadIdentity();
        GL11.glScalef(xScale, yScale, 1);
        GL11.glTranslatef(xOffset, yOffset, 0);
    }

    private void handleCameraPan(double delta) {
        Vector2d distanceLT = new Vector2d(nextLeft - left, 
                                           nextTop - top);
        Vector2d distanceRB = new Vector2d(nextRight - right, 
                                           nextBottom - bottom);
        
        Vector2d directionLT = new Vector2d();
        Vector2d directionRB = new Vector2d();
        distanceLT.normalize(directionLT);
        distanceRB.normalize(directionRB);
        
        float speed = (float) (panSpeed * delta);
        
        if (Math.abs(distanceLT.x) > MIN_DIST) {
            left += directionLT.x * speed;
        }
        
        if (Math.abs(distanceRB.x) > MIN_DIST) {
            right += directionRB.x * speed;
        }
        
        if (Math.abs(distanceLT.y) > MIN_DIST) {
            top += directionLT.y * speed;
        }
        
        if (Math.abs(distanceRB.y) > MIN_DIST) {
            bottom += directionRB.y * speed;
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.scenes;

import org.joml.Vector2d;
import org.lwjgl.opengl.GL11;
import sketchwars.game.GameObject;
import sketchwars.input.MouseState;
import sketchwars.input.MouseHandler;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class Camera implements GameObject {
    private static final float MIN_DIST = 0.001f;
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
    private float xScale;
    private float yScale;
        
    private boolean panning;
    private float xMouseFalling;
    private float yMouseFalling;
    
    private boolean expired;
    
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
        
        this.xScale = worldWidth/this.width;
        this.yScale = worldHeight/this.height;
        
        expired = false;
    }

    public void setPanning(boolean panning) {
        this.panning = panning;
    }

    public boolean isPanning() {
        return panning;
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
        
        this.xScale = worldWidth/width;
        this.yScale = worldHeight/height;
    }
    
    public void setCameraPosition(float xCenter, float yCenter) {
        float xOffset = width/2.0f;
        float yOffset = height/2.0f;
        
        float maxLeft = worldRight - width;
        float minTop = worldBottom + height;
        
        left = Math.min(Math.max(xCenter - xOffset, worldLeft), maxLeft);
        right = left + width;
        top = Math.max(Math.min(yCenter + yOffset, worldTop), minTop);
        bottom = top - height;
    }
    
    private void getClippedLeft(float left) {
        return;
    }
    
    public void setNextCameraPosition(float xCenter, float yCenter) {
        float xOffset = width/2.0f;
        float yOffset = height/2.0f;
        
        float maxLeft = worldRight - width;
        float minTop = worldBottom + height;
        
        nextLeft = Math.min(Math.max(xCenter - xOffset, worldLeft), maxLeft);
        nextRight = nextLeft + width;
        nextTop = Math.max(Math.min(yCenter + yOffset, worldTop), minTop);
        nextBottom = nextTop - height;
    }
    
    @Override
    public void update(double delta) {
        if (panning) {
            handleCameraPan(delta);
        }
    }
    
    public void applyCameraSettings() {
        Vector2d offset = getOffset();
        
        GL11.glLoadIdentity();
        GL11.glScalef(xScale, yScale, 1);
        GL11.glTranslatef((float)offset.x, (float)offset.y, 0);
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
        
        boolean reachedLeft = Math.abs(distanceLT.x) < MIN_DIST;
        boolean reachedRight = Math.abs(distanceRB.x) < MIN_DIST;
        boolean reachedTop = Math.abs(distanceLT.y) < MIN_DIST;
        boolean reachedBottom = Math.abs(distanceRB.y) < MIN_DIST;
        
        if (!reachedLeft) {
            left += directionLT.x * speed;
        }
        
        if (!reachedRight) {
            right += directionRB.x * speed;
        }
        
        if (!reachedTop) {
            top += directionLT.y * speed;
        }
        
        if (!reachedBottom) {
            bottom += directionRB.y * speed;
        }
        
        if (reachedLeft && reachedRight && reachedTop && reachedBottom) {
            panning = false;
        }
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
    
    @Override
    public boolean hasExpired() {
        return expired;
    }

    private Vector2d getOffset() {
        Vector2d offset = new Vector2d();
        float xOffset = -(left + (width / 2.0f));
        float yOffset = -(top - (height / 2.0f));
            
        if (MouseHandler.rightBtnState == MouseState.FALLING) {
            xMouseFalling = MouseHandler.xNormalized;
            yMouseFalling = MouseHandler.yNormalized;
        }
        
        if (MouseHandler.rightBtnState == MouseState.DOWN) {
            float xMouse = MouseHandler.xNormalized;
            float yMouse = MouseHandler.yNormalized;
            float xDelta = xMouseFalling - xMouse;
            float yDelta = yMouseFalling - yMouse;
            
            float xNewOffset = xOffset - xDelta;
            float yNewOffset = yOffset - yDelta;
            
            offset.set(xNewOffset, yNewOffset);
        } else {
            offset.set(xOffset, yOffset);
        }
        
        return offset;
    }

    public float getWidth() {
        return width;
    }
    
    public float getHeight() {
        return height;
    }

    public float getWorldLeft() {
        return worldLeft;
    }

    public float getWorldTop() {
        return worldTop;
    }

    public float getLeft() {
        return left;
    }
    
    public float getTop() {
        return top;
    }
}

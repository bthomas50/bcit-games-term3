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
    private static final float SLOW_RADIUS = 0.05f;
    private static final float REACHED_RADIUS = 0.01f;
    
    private static final float MAX_PAN_ACCEL = 0.01f;
    private static final float MAX_ZOOM_ACCEL = 0.01f;
    private static final float PAN_SPEED = 0.0020f;
    private static final float ZOOM_SPEED = 0.0020f;
    
    private float panSpeed;
    private float zoomSpeed;
    private Vector2d panVelocity;
    private Vector2d zoomVelocity;
    
    private final float worldLeft;
    private final float worldRight;
    private final float worldTop;
    private final float worldBottom;

    private final float worldWidth;
    private final float worldHeight;
    
    private float left;
    private float top;
    
    private float nextLeft;
    private float nextTop;
    
    private float width;
    private float height;
    private float nextWidth;
    private float nextHeight;
    
    private boolean panning;
    private boolean zooming;
    private float xMouseFalling;
    private float yMouseFalling;
    
    private boolean expired;
    
    private boolean dragReset;
    
    public Camera(float worldLeft, float worldTop, float worldWidth, float worldHeight) {
        this.worldLeft = worldLeft;
        this.worldRight = worldLeft + worldWidth;
        this.worldTop = worldTop;
        this.worldBottom = worldTop - worldHeight;
                
        this.panSpeed = PAN_SPEED;
        this.zoomSpeed = ZOOM_SPEED;
        
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.width = this.worldWidth;
        this.height = this.worldHeight;
        this.nextWidth = this.width;
        this.nextHeight = this.height;
    
        this.left = this.worldLeft;
        this.top = this.worldTop;
        
        this.nextLeft = this.left;
        this.nextTop = this.top;
        
        this.expired = false;
        
        this.panVelocity = new Vector2d();
        this.zoomVelocity = new Vector2d();
        this.dragReset = true;
    }

    public void toggleDragReset() {
        dragReset = !dragReset;
    }
    
    public void setDragReset(boolean value) {
        dragReset = value;
    }
    
    public boolean isDragResetOn() {
        return dragReset;
    }
    
    public boolean isPanning() {
        return panning;
    }
    
    public boolean isZooming() {
        return zooming;
    }

    public float getPanSpeed() {
        return panSpeed;
    }

    public void setPanSpeed(float panSpeed) {
        this.panSpeed = panSpeed;
    }

    public void setZoomSpeed(float zoomSpeed) {
        this.zoomSpeed = zoomSpeed;
    }
    
    public void setCameraSize(float width, float height) {
        this.width = width;
        this.height = height;
    }
    
    public void setNextCameraSize(float width, float height) {
        this.nextWidth = width;
        this.nextHeight = height;
        zooming = true;
    }
    
    public void setCameraPosition(float xCenter, float yCenter) {
        left = getBoundedXFromCenter(xCenter);
        top = getBoundedYFromCenter(yCenter);
    }
    
    private float getBoundedXFromCenter(float xCenter) {
        float xOffset = width/2.0f;
        float maxLeft = worldRight - width;
        return (float)Math.min(Math.max(xCenter - xOffset, worldLeft), maxLeft);
    }
    
    private float getBoundedYFromCenter(float yCenter) {
        float yOffset = height/2.0f;
        float minTop = worldBottom + height;
        return (float)Math.max(Math.min(yCenter + yOffset, worldTop), minTop);
    }
    
    private float getBoundedX(float x) {
        float maxLeft = worldRight - width;
        float minLeft = worldLeft;
        return (float)Math.min(Math.max(x, minLeft), maxLeft);
    }
    
    private float getBoundedY(float y) {
        float minTop = worldBottom + height;
        float maxTop = worldTop;
        return (float)Math.max(Math.min(y, maxTop), minTop);
    }
        
    public void setNextCameraPosition(float xCenter, float yCenter) {
        nextLeft = getBoundedXFromCenter(xCenter);
        nextTop = getBoundedYFromCenter(yCenter);
        panning = true;
    }
    
    @Override
    public void update(double delta) {
        if (zooming) {
            handleCameraZooming(delta);
        }
        
        if (panning && !zooming) {
            handleCameraPan(delta);
        }
    }
    
    public void applyCameraSettings() {
        Vector2d offset = getOffset();
        
        GL11.glLoadIdentity();
        GL11.glOrtho(worldLeft, worldRight, 
                worldBottom, worldTop, -1.0, 1.0);
        
        GL11.glScalef(worldWidth/width, worldHeight/height, 1);
        GL11.glTranslatef((float)offset.x, (float)offset.y, 0);
    }

    private Vector2d getOffset() {
        if (MouseHandler.rightBtnState == MouseState.FALLING) {
            xMouseFalling = MouseHandler.xNormalized;
            yMouseFalling = MouseHandler.yNormalized;
        } else if (MouseHandler.rightBtnState == MouseState.DOWN) {
            float xMouse = MouseHandler.xNormalized;
            float yMouse = MouseHandler.yNormalized;
            float xDragOffset = xMouseFalling - xMouse;
            float yDragOffset = yMouseFalling - yMouse;
           
            left = getBoundedX(left + xDragOffset);
            top = getBoundedY(top + yDragOffset);
        }
        
        Vector2d offset = new Vector2d();
        float xOffset = -(left + (width / 2.0f));
        float yOffset = -(top - (height / 2.0f));
        offset.set(xOffset, yOffset);
        
        return offset;
    }

    private void handleCameraPan(double delta) {
        Vector2d distance = new Vector2d(nextLeft - left, 
                                         nextTop - top);
        
        Vector2d direction = new Vector2d();
        distance.normalize(direction);
        
        float dist = (float) distance.length();
        
        float targetSpeed = panSpeed;
        if (dist < SLOW_RADIUS) {
            targetSpeed = (panSpeed * dist/SLOW_RADIUS);
        }
        
        if (dist < REACHED_RADIUS) {
            panning = false;
            panVelocity = new Vector2d();
        } else {
            Vector2d linearAccel = new Vector2d(direction.x * MAX_PAN_ACCEL, 
                                                direction.y * MAX_PAN_ACCEL);
           
            panVelocity.x += linearAccel.x * delta;
            panVelocity.y += linearAccel.y * delta;
            
            if (panVelocity.length() > targetSpeed) {
                panVelocity.normalize();
                panVelocity = new Vector2d(panVelocity.x * targetSpeed,
                                           panVelocity.y * targetSpeed);
            }
            
            left += panVelocity.x * delta;
            top += panVelocity.y * delta;
        }
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
    
    @Override
    public boolean hasExpired() {
        return expired;
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

    public float getWorldWidth() {
        return worldWidth;
    }

    public float getWorldHeight() {
        return worldHeight;
    }

    private void handleCameraZooming(double delta) {        
        Vector2d distance = new Vector2d(nextWidth - width, 
                                         nextHeight - height);
        
        Vector2d direction = new Vector2d();
        distance.normalize(direction);
        
        float targetSpeed = zoomSpeed;
        
        float dist = (float) distance.length();
        
        if (dist < SLOW_RADIUS) {
            targetSpeed = (zoomSpeed * dist/SLOW_RADIUS);
        }
                
        if (dist < REACHED_RADIUS) {
            zooming = false;
            zoomVelocity = new Vector2d();
        } else {
            Vector2d linearAccel = new Vector2d(direction.x * MAX_ZOOM_ACCEL, 
                                                direction.y * MAX_ZOOM_ACCEL);
           
            zoomVelocity.x += linearAccel.x * delta;
            zoomVelocity.y += linearAccel.y * delta;
            
            if (zoomVelocity.length() > targetSpeed) {
                zoomVelocity.normalize();
                zoomVelocity = new Vector2d(zoomVelocity.x * targetSpeed,
                                            zoomVelocity.y * targetSpeed);
            }
            
            float xVelocity = (float) (zoomVelocity.x * delta);
            float yVelocity = (float) (zoomVelocity.y * delta);
            
            width += xVelocity;
            height += yVelocity;
            
            left -= xVelocity/2.0f;
            top += yVelocity/2.0f;
        }
    }

    public float getWorldRight() {
        return worldRight;
    }

    public float getWorldBottom() {
        return worldBottom;
    }

    public float getCenterX() {
        return left + width/2.0f;
    }
    
    public float getCenterY() {
        return top - height/2.0f;
    }
}

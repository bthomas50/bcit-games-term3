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
    private static final float SLOW_RADIUS = 0.01f;
    private static final float REACHED_RADIUS = 0.001f;
    
    private static final float PAN_SPEED = 0.0008f;
    
    private float panSpeed;
    
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
                
        this.panSpeed = PAN_SPEED;
        
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.width = this.worldWidth;
        this.height = this.worldHeight;
        
        this.left = this.worldLeft;
        this.top = this.worldTop;
        
        this.nextLeft = this.left;
        this.nextTop = this.top;
        
        this.xScale = worldWidth/this.width;
        this.yScale = worldHeight/this.height;
        
        this.expired = false;
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
        float xOffset = width/2.0f;
        float maxLeft = worldRight - xOffset;
        float minLeft = worldLeft + xOffset;
        return (float)Math.min(Math.max(x, minLeft), maxLeft);
    }
    
    private float getBoundedY(float y) {
        float yOffset = height/2.0f;
        float minTop = worldBottom + yOffset;
        float maxTop = worldTop - yOffset;
        return (float)Math.max(Math.min(y, maxTop), minTop);
    }
        
    public void setNextCameraPosition(float xCenter, float yCenter) {
        nextLeft = getBoundedXFromCenter(xCenter);
        nextTop = getBoundedYFromCenter(yCenter);
        panning = true;
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
        Vector2d distance = new Vector2d(nextLeft - left, 
                                           nextTop - top);
        
        Vector2d direction = new Vector2d();
        distance.normalize(direction);
        
        float speed = panSpeed;
        
        float dist = (float) distance.length();
        
        if (dist < SLOW_RADIUS) {
            speed = (panSpeed * dist/SLOW_RADIUS);
        }
                
        if (dist < REACHED_RADIUS) {
            panning = false;
        } else {
            Vector2d velocity = new Vector2d(direction.x * speed, 
                                               direction.y * speed);

            left += velocity.x * delta;
            top += velocity.y * delta;
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
            
            float xNewOffset = getBoundedX(xOffset - xDelta);
            float yNewOffset = getBoundedY(yOffset - yDelta);
            
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

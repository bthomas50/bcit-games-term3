package sketchwars.scenes;

import java.util.ArrayList;
import sketchwars.game.GameObject;
import sketchwars.animation.Animation;
import sketchwars.animation.Explosion;
import sketchwars.graphics.GraphicsObject;

/**
 * Used in the scene as a layer of graphics objects
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class Layer implements GraphicsObject, GameObject, Comparable<Layer> {
    private final ArrayList<GraphicsObject> drawableObjs;
    private final ArrayList<Animation> animations;
    private int zOrder;
    
    public Layer() {
        drawableObjs = new ArrayList<>();
        animations = new ArrayList<>();
    }
    
    public void addAnimation(Explosion animation) {
        animations.add(animation);
        addDrawableObject(animation);
    }

    private void updateAnimations(double delta) {
        for (Animation a: animations) {
            a.update(delta);
        }
    }

    private void removeExpiredAnimations() {
        int size = animations.size();
        
        for (int i = size - 1; i >= 0; i--) {
            Animation animation = animations.get(i);
            
            if (animation.hasExpired()) {
                removeDrawableObject(animation);
                animations.remove(i);
            }
        }
    }
    
     public void addDrawableObject(GraphicsObject obj) {
        drawableObjs.add(obj);
    }
    
    public void removeDrawableObject(GraphicsObject obj) {
        drawableObjs.remove(obj);
    }
        
    @Override
    public void render() {
        for (GraphicsObject obj : drawableObjs) {
            obj.render();
        }
        for (GraphicsObject obj : animations) {
            obj.render();
        }
    }

    @Override
    public void update(double delta) {
        updateAnimations(delta);
        removeExpiredAnimations();
    }

    public int getZOrder() {
        return zOrder;
    }

    /**
     * Set z-order before adding a layer to a scene
     * Note: Use Scene class setZOrder method to change 
     * z-order after adding a layer to the scene
     * @param zOrder new z-order
     */
    public void setZOrder(int zOrder) {
        this.zOrder = zOrder;
    }
    
    @Override
    public int compareTo(Layer other) {
        if (getZOrder() < other.getZOrder()) {
            return -1;
        } else  if (getZOrder() > other.getZOrder()) {
            return  1;
        }
        
        return 0;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.animation;

import java.util.HashMap;
import org.joml.Vector2d;
import sketchwars.game.GameObject;
import sketchwars.graphics.GraphicsObject;

/**
 * Set of animations
 * @author Najash Najimudeen <najash.najm@gmail.com>
 * @param <T> animation index type
 */
public class AnimationSet<T> implements GameObject, GraphicsObject {
    private final HashMap<T, Animation> animations;

    private Animation currentAnimation;
    
    public AnimationSet() {
        animations = new HashMap<>();
    }
    
    /**
     * remove all animations.
     */
    public void clear() {
        animations.clear();
    }
    
    @Override
    public void update(double delta) {
        if (currentAnimation != null) {
            currentAnimation.update(delta);
        }
    }

    @Override
    public void render() {
        if (currentAnimation != null) {
            currentAnimation.render();
        }
    }
    
    public void addAnimation(T index, Animation animation) {
        animations.put(index, animation);
    }
    
    public void setCurrentAnimation(T index) {
        currentAnimation = animations.get(index);
        
        if (currentAnimation == null) {
            System.err.println("Given animation does not exist.");
        }
    }
    
    /**
     * Set the animation screen location for the current animation
     * @param pos new position
     */
    public void setAnimationPosition(Vector2d pos) {
        if (currentAnimation != null) {
            currentAnimation.setPosition(pos);
        } else {
            System.err.println("Current animation is not set.");
        }
    }

    public void setAnimationDimension(Vector2d dimension) {
        if (currentAnimation != null) {
            currentAnimation.setDimension(dimension);
        } else {
            System.err.println("Current animation is not set.");
        }
    }

    public boolean isEmpty() {
        return animations.isEmpty();
    }

    public Animation getCurrentAnimation() {
        return currentAnimation;
    }

    public Animation getAnimation(T index) {
        return animations.get(index);
    }
}

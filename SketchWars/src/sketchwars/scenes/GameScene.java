/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.scenes;

import java.util.ArrayList;
import sketchwars.Animation;
import sketchwars.Explosion;


/**
 * The main game scene
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class GameScene extends AbstractScene {
    private final ArrayList<Animation> animations;

    public GameScene() {
        animations = new ArrayList<>();
    }

    @Override
    public void update(double delta) {
        updateAnimations(delta);
        removeExpiredAnimations();
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
}

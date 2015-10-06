package sketchwars.scenes;

import java.util.ArrayList;
import sketchwars.graphics.GraphicsObject;

/**
 * Use this class to create custom scenes
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public abstract class AbstractScene {
    private final ArrayList<GraphicsObject> drawableObjs;

    public AbstractScene() {
        drawableObjs = new ArrayList<>();
    }
    
    public void addDrawableObject(GraphicsObject obj) {
        drawableObjs.add(obj);
    }
        
    public void render() {
        for (GraphicsObject obj : drawableObjs) {
            obj.render();
        }
    }
    
    public abstract void update(double delta);
}

package sketchwars.graphics;

import java.util.ArrayList;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class Graphics {
    private ArrayList<GraphicsObject> drawableObjs;
    
    public void init() {
        drawableObjs = new ArrayList<>();
    }
    
    public void AddDrwableObject(GraphicsObject obj) {
        drawableObjs.add(obj);
    }
    
    public void Render() {
        for (GraphicsObject obj : drawableObjs) {
            obj.render();
        }
    }
}

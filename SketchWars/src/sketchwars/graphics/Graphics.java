package sketchwars.graphics;

import java.util.ArrayList;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class Graphics {
    private ArrayList<Drawable> drawableObjs;
    
    public void init() {
        drawableObjs = new ArrayList<>();
    }
    
    public void AddDrwableObject(Drawable obj) {
        drawableObjs.add(obj);
    }
    
    public void Render() {
        for (Drawable obj : drawableObjs) {
            obj.render();
        }
    }
}

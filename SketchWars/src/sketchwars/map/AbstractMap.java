package sketchwars.map;

import sketchwars.graphics.GraphicsObject;
import sketchwars.graphics.Texture;
import sketchwars.physics.*;
import sketchwars.GameObject;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public abstract class AbstractMap implements GraphicsObject, GameObject {
    Texture background;
    Texture texture;
    
    Collider coll;

    public void setCollider(Collider coll) {
        this.coll = coll;
    }

    public abstract void init();
    
    @Override
    public void update(double elapsedMillis) {

    }

    @Override
    public void render() {
        if(background != null) {
            background.drawNormalized(0, 0, 1, 1);
        }
        if (texture != null) {
            BoundingBox bounds = coll.getBounds();
            long vCenter = bounds.getCenterVector();
            texture.drawNormalized(0, 0, 1, 1);
        }
    }
    
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}

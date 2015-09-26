package sketchwars.character.projectiles;

import sketchwars.graphics.GraphicsObject;
import sketchwars.graphics.Texture;
import sketchwars.physics.*;
import sketchwars.GameObject;
/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public abstract class AbstractProjectile implements GraphicsObject, GameObject{

    Collider coll;
    Texture texture;
        
    public abstract void init();
    
    
    @Override
    public void update(double elapsedMillis) {

    }

    public void setCollider(Collider coll) {
        this.coll = coll;
    }

    @Override
    public void render() {
        if (texture != null) {
            BoundingBox bounds = coll.getBounds();
            texture.drawNormalized((double)bounds.getLeft() / 1024.0, (double) bounds.getTop() / 1024.0, (double) bounds.getWidth() / 2048.0, (double) bounds.getHeight() / 2048.0);
        }
    }
    
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}

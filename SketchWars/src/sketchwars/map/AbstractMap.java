package sketchwars.map;

import sketchwars.graphics.GraphicsObject;
import sketchwars.graphics.Texture;
import sketchwars.physics.*;
import sketchwars.game.GameObject;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public abstract class AbstractMap implements GraphicsObject, GameObject {
    
    @Override
    public void update(double elapsedMillis) 
    {}

    @Override
    public boolean hasExpired()
    {
        return false;
    }

    public abstract void dispose();
}

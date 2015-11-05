package sketchwars.map;

import java.awt.image.BufferedImage;
import sketchwars.graphics.Texture;
import sketchwars.physics.MapCollider;
import sketchwars.scenes.Camera;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 * @author Brian
 */
public class TestMap extends AbstractMap {
    
    public TestMap(Camera camera, MapCollider mapCollider, Texture background, Texture foreground, BufferedImage foregroundImage) {
        super(camera, mapCollider, background, foreground, foregroundImage);
    }
}

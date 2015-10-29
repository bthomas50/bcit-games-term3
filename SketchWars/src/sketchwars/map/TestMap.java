package sketchwars.map;

import java.awt.image.BufferedImage;
import sketchwars.graphics.Texture;
import sketchwars.physics.MapCollider;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 * @author Brian
 */
public class TestMap extends AbstractMap {
    
    public TestMap(MapCollider mapCollider, Texture background, Texture foreground, BufferedImage foregroundImage) {
        super(mapCollider, background, foreground, foregroundImage);
    }
}

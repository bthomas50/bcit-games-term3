package sketchwars.map;

import sketchwars.graphics.Texture;
import sketchwars.physics.*;
/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 * @author Brian
 */
public class TestMap extends AbstractMap {
    
    Texture background;
    Texture foreground;
    
    public TestMap(Texture foreground, Texture background) {
        this.foreground = foreground;
        this.background = background;
    }

    @Override
    public void render() {
        background.drawNormalized(null, 0, 0, 1, 1);
        foreground.drawNormalized(null, 0, 0, 1, 1);
    }

    @Override
    public void dispose() {
        background.dispose();
        foreground.dispose();
    }
}

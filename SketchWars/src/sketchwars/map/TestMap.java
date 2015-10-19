package sketchwars.map;

import sketchwars.graphics.Texture;

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
        background.draw(null, 0, 0, 2, 2);
        foreground.draw(null, 0, 0, 2, 2);
    }

    @Override
    public void dispose() {
        background.dispose();
        foreground.dispose();
    }
}

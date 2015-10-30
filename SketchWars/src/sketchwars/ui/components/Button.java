package sketchwars.ui.components;

import org.joml.Vector2d;
import sketchwars.graphics.Texture;
import sketchwars.input.MouseHandler;
import sketchwars.input.KeyState;


/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class Button extends UIComponent {
    private final Texture normalTexture;
    private final Texture pressedTexture;
    private final Texture highlightTexture;

    public Button(Vector2d position, Vector2d size, Texture normal, Texture pressed, Texture highlight) {
        super(position, size, null, true);
        
        normalTexture = normal;
        
        if (pressed == null) {
            pressedTexture = normal;
        } else {
            pressedTexture = pressed;
        }
        
        if (highlight == null) {
            highlightTexture = normal;
        } else {
            highlightTexture = highlight;
        }
    }

    @Override
    public void render()
    {
        super.render();
        
        if (mouseInComponent) {
            if(pressedTexture != null && MouseHandler.state == KeyState.DOWN) {
                pressedTexture.draw(null, (float)position.x, (float)position.y, (float)size.x, (float)size.y);
            } else if (highlightTexture != null) {
                highlightTexture.draw(null, (float)position.x, (float)position.y, (float)size.x, (float)size.y);
            }
        } else {
            if (normalTexture != null) {
                normalTexture.draw(null, (float)position.x, (float)position.y, (float)size.x, (float)size.y);
            }
        }
    }

    @Override
    public void redraw() { }
};
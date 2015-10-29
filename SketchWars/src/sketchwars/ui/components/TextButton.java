/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.ui.components;

import java.awt.Font;
import org.joml.Vector2d;
import sketchwars.graphics.Texture;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class TextButton extends Button {
    private final Label label;
            
    /**
     * 
     * @param text
     * @param font can be null (will use default font if available)
     * @param position
     * @param size
     * @param normal
     * @param pressed
     * @param highlight 
     */
    public TextButton(String text, Font font, Vector2d position, Vector2d size, 
            Texture normal, Texture pressed, Texture highlight) {
        super(position, size, normal, pressed, highlight);
        
        label = new Label(text, font, position, size, null);
    }

    @Override
    public void render() {
        super.render();
        
        if (label != null) {
            label.render();
        }
    }
}

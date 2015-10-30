/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.ui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import org.joml.Vector2d;
import sketchwars.graphics.Texture;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class Label extends UIComponent {
    private String text; 
    private Texture label;
    
    public Label(String text, Font font, Vector2d position, Vector2d size, Texture background) {
        super(position, size, background, false);
        
        this.text = text;
        this.font = font;
        
        fontColor = Color.BLACK;
        createLabel();
    }

    public String getText() {
        return text;
    }
    
    public Texture getLabel() {
        return label;
    }

    public void setText(String text) {
        this.text = text;
        redraw();
    }

    @Override
    public void render() {
        super.render();
        
        if (label != null) {
            label.draw(null, (float)position.x, (float)position.y, (float)size.x, (float)size.y);
        }
    }

    private void createLabel() {
        if (text != null) {
            BufferedImage labelImg = createLabelImage(text, font, fontColor);
            
            if (labelImg != null) {
                label = new Texture(labelImg, false);
            } else {
                System.err.println("Label:createLabel(): Error craeting label");
            }
        }
    }

    @Override
    public void redraw() {
        createLabel();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.ui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.joml.Vector2d;
import sketchwars.graphics.Texture;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class Label extends UIComponent {
    private String text; 
    private Font font;
    private Texture label;
    private Color fontColor;
    
    public Label(String text, Font font, Vector2d position, Vector2d size, Texture background) {
        super(position, size, background, false);
        
        this.text = text;
        this.font = font;
        
        createLabel();
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
        createLabel();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        createLabel();
    }

    public void setBackground(Texture background) {
        this.background = background;
    }

    public void setFont(Font font) {
        this.font = font;
        createLabel();
    }
    
    @Override
    public void render() {
        super.render();
        
        if (label != null) {
            label.draw(null, (float)position.x, (float)position.y, (float)size.x, (float)size.y);
        }
    }

    @Override
    void update() {}

    private void createLabel() {
        if (text != null && !text.isEmpty()) {
            BufferedImage labelImg = createLabelImage(text, font, fontColor);
            label = new Texture(labelImg, false);
            
            try {
                // retrieve image
                File outputfile = new File("f:/saved.png");
                ImageIO.write(labelImg, "png", outputfile);
            } catch (IOException e) {
                
            }
        } else {
            System.err.println("Error creating label texture.");
        }
    }
}

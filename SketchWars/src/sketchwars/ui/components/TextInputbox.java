/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.ui.components;

import java.awt.Color;
import java.awt.image.BufferedImage;
import org.joml.Vector2d;
import sketchwars.graphics.Texture;
import static org.lwjgl.glfw.GLFW.*;
import sketchwars.input.KeyCharListener;
import sketchwars.input.KeyboardHandler;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class TextInputbox extends UIComponent implements KeyCharListener {
    private static final int PRESS_RATE = 160;
    
    private Texture label;
    private String text;
    private int caretPos;
    
    private long lastKeyPressTime;
    
    private final Texture borderNotSelected;
    private final Texture borderSelected;
    
    public TextInputbox(Vector2d position, Vector2d size, Texture background) {
        super(position, size, background, true);
        
        borderNotSelected = Texture.loadTexture("content/uicomponents/textbox.png", false);
        borderSelected = Texture.loadTexture("content/uicomponents/textboxSelected.png", false);
        
        text = "";
        updateLabel();
        setBackgroundFromColor(Color.LIGHT_GRAY);
        
        lastKeyPressTime = System.currentTimeMillis();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (text != null) {
            this.text = text;
            caretPos = text.length();
            redraw();
        }
    }

    @Override
    public void render() {
        super.render();
        
        if (selected && borderSelected != null) {
            borderSelected.draw(null, (float)position.x, (float)position.y, (float)size.x, (float)size.y);
        } else if (borderNotSelected != null) {
            borderNotSelected.draw(null, (float)position.x, (float)position.y, (float)size.x, (float)size.y);
        }
        
        if (label != null) {
            label.draw(null, (float)position.x, (float)position.y, (float)size.x, (float)size.y);
        }
    }

    @Override
    public void update() {
        super.update(); 
        
        if (selected) {
            handleInput();
        }
    }
    
    private void updateLabel() {
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
    public void charTyped(int key) {
        if (selected) {
            text += (char)key;
            redraw();
            caretPos++;
        }
    }

    private void handleInput() {
        long pressTime = System.currentTimeMillis();
        int delta = (int) (pressTime - lastKeyPressTime);
        
        if (KeyboardHandler.isKeyDown(GLFW_KEY_BACKSPACE) && delta > PRESS_RATE) {
            int length = text.length();

            if (caretPos > 0 && caretPos <= length) {
                caretPos--;
                text = text.substring(0, caretPos) + text.substring(caretPos + 1);
                redraw();
            }
            
            lastKeyPressTime = pressTime;
        }
    }

    @Override
    public void redraw() {
        updateLabel();
    }
}

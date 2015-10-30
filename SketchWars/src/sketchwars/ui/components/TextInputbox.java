/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.ui.components;

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
    
    public TextInputbox(Vector2d position, Vector2d size, Texture background) {
        super(position, size, background, true);
        
        if (background == null) {
            setBackground(Texture.loadTexture("content/uicomponents/textbox.png", false));
        }
        
        text = "";
        updateLabel(text);
        
        KeyboardHandler.addCharListener((TextInputbox)this);
        lastKeyPressTime = System.currentTimeMillis();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        updateLabel(text);
    }

    @Override
    public void render() {
        super.render();
        
        if (label != null) {
            label.draw(null, (float)position.x, (float)position.y, (float)size.x, (float)size.y);
        }
    }

    @Override
    public void update() {
        super.update(); 
        
        handleInput();
    }
    
    private void updateLabel(String text) {
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
            updateLabel(text);
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
                updateLabel(text);
            }
            
            lastKeyPressTime = pressTime;
        }
    }
}

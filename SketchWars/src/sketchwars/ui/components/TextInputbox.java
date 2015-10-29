/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.ui.components;

import org.joml.Vector2d;
import sketchwars.graphics.Texture;
import sketchwars.input.KeyState;
import sketchwars.input.KeyboardHandler;
import static org.lwjgl.glfw.GLFW.*;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class TextInputbox extends UIComponent {
    public static int STANDARD_KEY_START = 32;
    public static int STANDARD_KEY_END = 126;
    
    //private Texture label;
    private String text;
    private int caretPos;
    
    private Label label;
    
    private long lastKeyPressTime;
    
    public TextInputbox(Vector2d position, Vector2d size, Texture background) {
        super(position, size, background, true);
        
        if (background == null) {
            setBackground(Texture.loadTexture("content/uicomponents/textbox.png", false));
        }
        
        label = new Label(text, font, position, size, background);
        
        text = "";
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        updateLabel(text);
    }

    @Override
    void update() {
        if (selected) {
            handleInput();
        }
    }
    
    @Override
    public void render() {
        super.render();
        
        label.render();
        /*if (label != null) {
            label.draw(null, (int)position.x, (int)position.y, (int)size.x, (int)size.y);
        }*/
    }
    
    private void handleInput() {
        if (KeyboardHandler.state == KeyState.FALLING) {
            int key = KeyboardHandler.key;
            if (key >= STANDARD_KEY_START && key <= STANDARD_KEY_END) {
                text += (char)key;
                updateLabel(text);
                caretPos++;
            } else if (key == GLFW_KEY_BACKSPACE) {
                int length = text.length();

                if (caretPos > 0 && caretPos <= length) {
                    caretPos--;
                    text = text.substring(0, caretPos) + text.substring(caretPos + 1);
                    updateLabel(text);
                }
            }
        }
    }
    
    private void updateLabel(String text) {
        label.setText(text);
        
       /* BufferedImage img = createLabelImage(text, font, fontColor);
        
        if (img != null) {
            label = new Texture(img, false);
        } else {
            System.err.println("TextInputBox:updateLabel(): Error craeting label");
        }*/
    }
}

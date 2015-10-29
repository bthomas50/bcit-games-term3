/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.ui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import org.joml.Vector2d;
import sketchwars.OpenGL;
import sketchwars.graphics.GraphicsObject;
import sketchwars.graphics.Texture;
import sketchwars.input.MouseHandler;
import sketchwars.physics.BoundingBox;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public abstract class UIComponent implements GraphicsObject {    
    private final ArrayList<UIActionListener> listeners;
    protected Vector2d position;
    protected Vector2d size;
    protected boolean mouseInComponent;
    protected Texture background;
    
    protected boolean handleInput;
    protected boolean enabled;
    protected boolean selected;
    
    protected Color fontColor;
    protected Font font;
    
    public UIComponent(Vector2d position, Vector2d size, Texture background, boolean handleInput) {
        this.position = position;
        this.size = size;
        this.background = background;
        this.handleInput = handleInput;
        enabled = true;
        
        listeners = new ArrayList<>();
    }
    
    public void addActionListener(UIActionListener listener) {
        listeners.add(listener);
    }
    
    public void removeActionListener(UIActionListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }
    
    protected void notifyListeners() {
        if (enabled) {
            for (UIActionListener listener: listeners) {
                listener.action(this);
            }
        }
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public Vector2d getPosition() {
        return position;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public Vector2d getSize() {
        return size;
    }

    public void setSize(Vector2d size) {
        this.size = size;
    }

    public boolean isMouseInComponent() {
        return mouseInComponent;
    }

    public void setHandleInput(boolean handleInput) {
        this.handleInput = handleInput;
    }
    
    public void removeAllActionListeners() {
        listeners.clear();
    }

    public void setBackground(Texture background) {
        this.background = background;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public boolean contains(float x, float y) 
    {
        return x > position.x - size.x/2 && x < position.x + size.x/2 && 
               y < position.y + size.y/2 && y > position.y - size.y/2;
    }

    @Override
    public boolean hasExpired() {
        return false; //UI components dont expire
    }

    @Override
    public void render() {
        if (background != null) {
            background.draw(null, (float)position.x, (float)position.y, (float)size.x, (float)size.y);
        }
        
        if (enabled) {
            update();

            if (handleInput) {
                handleInput();
            }
        }
    }
        
    abstract void update();
    
    private void handleInput() {
        float xMouse = MouseHandler.xNormalized;
        float yMouse = MouseHandler.yNormalized;
        
        mouseInComponent = contains(xMouse, yMouse);
    }
    
    public BufferedImage createLabelImage(String text, Font font, Color fontColor) {
        if (text != null) {
            Vector2d screenSize = OpenGL.getDisplaySize();
            
            float graphicsWidth = OpenGL.GRAPHICS_BOUNDS.getWidth();
            float graphicsHeight = OpenGL.GRAPHICS_BOUNDS.getHeight();
            int width = (int) Math.abs(size.x/graphicsWidth * screenSize.x);
            int height = (int) Math.abs(size.y/graphicsHeight * screenSize.y);
            
            BufferedImage buttonImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics g = buttonImg.getGraphics();
         
            if (font == null) {
                int fontSize = (int)(width * 0.2f);
                Font newFont = new Font("Arial", Font.BOLD, fontSize);
                g.setFont(newFont);
            } else {
                g.setFont(font);
            }
            
            if (fontColor == null) {
                g.setColor(Color.BLACK);
            } else {
                g.setColor(fontColor);
            }
            
            FontMetrics fontMetrics = g.getFontMetrics();
            int textWidth = fontMetrics.stringWidth(text);
            int textHeight = fontMetrics.getHeight();
            
            int x = (int)((width - textWidth)/2.0f);
            int y = height - (int)(((height - textHeight)/2.0f)) - (int)(textHeight/5f);
            g.drawString(text, x, y);
            return buttonImg;
        } else {
            System.err.println("Error creating label image.");
        }
        
        return null;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.ui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import org.joml.Vector2d;
import sketchwars.OpenGL;
import sketchwars.graphics.GraphicsObject;
import sketchwars.graphics.Texture;
import sketchwars.input.KeyState;
import sketchwars.input.MouseHandler;

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
    
    public double getLeft() {
        return position.x - size.x/2;
    }
    
    public double getRight() {
        return position.x + size.x/2;
    }
    
    public double getTop() {
        return position.y + size.y/2;
    }
    
    public double getBottom() {
        return position.y - size.y/2;
    }
    
    public boolean contains(UIComponent other)
    {
        return (other.getTop() <= getTop()) && 
               (other.getLeft() >= getLeft()) && 
               (other.getBottom() >= getBottom()) && 
               (other.getRight() <= getRight());
    }
    
    public boolean containsSomeParts(UIComponent other)
    {
        Vector2d topLeft = new Vector2d(other.getLeft(), other.getTop());
        Vector2d topRight = new Vector2d(other.getRight(), other.getTop());
        Vector2d bottomLeft = new Vector2d(other.getLeft(), other.getBottom());
        Vector2d bottomRight = new Vector2d(other.getRight(), other.getBottom());
        
        return (contains((float)topLeft.x, (float)topLeft.y)) || 
                contains((float)topRight.x, (float)topRight.y) || 
                contains((float)bottomLeft.x, (float)bottomLeft.y) ||
                contains((float)bottomRight.x, (float)bottomRight.y);
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
    
    protected void notifyListeners(float x, float y) {
        if (enabled) {
            for (UIActionListener listener: listeners) {
                listener.action(this,x , y);
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
        return x >= getLeft() && x <= getRight() && 
               y <= getTop() && y >= getBottom();
    }

    @Override
    public boolean hasExpired() {
        return false; //UI components dont expire
    }

    protected void renderBackground() {
        if (background != null) {
            background.draw(null, (float)position.x, (float)position.y, (float)size.x, (float)size.y);
        }
    }
    
    @Override
    public void render() {
        renderBackground();
        update();
    }
        
    public void update() {
        if (enabled) {
            if (handleInput) {
                handleInput();
            }
        }
    }
    
    private boolean handleInput() {
        float xMouse = MouseHandler.xNormalized;
        float yMouse = MouseHandler.yNormalized;
        
        mouseInComponent = contains(xMouse, yMouse);
        
        if (mouseInComponent && MouseHandler.state == KeyState.RISING) {
            notifyListeners(xMouse, yMouse);
            return true;
        }
        
        return false;
    }
    
    public BufferedImage createLabelImage(String text, Font font, Color fontColor) {
        if (text != null) {
            Vector2d screenSize = OpenGL.getDisplaySize();
            
            int width = (int) Math.abs(size.x * screenSize.x);
            int height = (int) Math.abs(size.y * screenSize.y);
            
            BufferedImage buttonImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) buttonImg.getGraphics();
         
            int fontSize = (int)(width * 0.16f);
            
            if (font == null) {
                g.setFont(new Font("Arial", Font.BOLD, fontSize));
            } else {
                g.setFont(font.deriveFont(Font.BOLD, fontSize));
            }
            
            if (fontColor == null) {
                g.setColor(Color.BLACK);
            } else {
                g.setColor(fontColor);
            }
            
            RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
                                                   RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setRenderingHints(rh);
            
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

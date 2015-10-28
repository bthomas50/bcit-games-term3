/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.ui;

import java.util.ArrayList;
import org.joml.Vector2d;
import sketchwars.graphics.GraphicsObject;
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
     
    public UIComponent(Vector2d position, Vector2d size) {
        this.position = position;
        this.size = size;
        
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
    
    protected void notifyListeners() {
        for (UIActionListener listener: listeners) {
            listener.action(this);
        }
    }
    
    public void removeAllActionListeners() {
        listeners.clear();
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
        update();
        handleInput();
    }
        
    abstract void update();
    
    private void handleInput() {
        float xMouse = MouseHandler.xNormalized;
        float yMouse = MouseHandler.yNormalized;
        
        if (contains(xMouse, yMouse)) {
            mouseInComponent = true;
        } else {
            mouseInComponent = false;
        }
    }
}

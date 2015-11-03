/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.ui.components;

import java.util.ArrayList;
import org.joml.Vector2d;
import sketchwars.input.KeyboardHandler;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class UIGroup extends UIComponent implements UIActionListener {
    private final ArrayList<UIComponent> components;
    
    /**
     * 
     * @param position can be null
     * @param size  can be null
     */
    public UIGroup(Vector2d position, Vector2d size) {
        super(position, size, null, true);
        
        components = new ArrayList<>();
        addActionListener((UIGroup)this);
    }
    
    public void addUIComponent(UIComponent component) {
        if (component != null) {
            components.add(component);
            
            if (component instanceof TextInputbox) {
                KeyboardHandler.addCharListener((TextInputbox)component);
            }
            component.addActionListener(this);
        }
    }
    
    public void removeUIComponent(UIComponent component) {
        if (component != null) {
            component.removeActionListener(this);
            
            if (component instanceof TextInputbox) {
                KeyboardHandler.removeCharListener((TextInputbox)component);
            }
            
            components.remove(component);
        }
    }

    @Override
    public void render() {
        if (visible) {
            super.render();

            for (UIComponent uic: components) {
                uic.render();
            }
        }
    }
    
    public void deselectAll() {
        for (UIComponent uic: components) {
            uic.setSelected(false);
        }
    }

    @Override
    public void action(UIComponent component, float x, float y) {
        if (component != null) {
            deselectAll();
            
            if (!(component instanceof UIGroup)) {
                component.setSelected(true);
            }
        }
    }

    @Override
    public void redraw() { 
        for (UIComponent uic: components) {
            uic.setFont(font);
            uic.setFontColor(fontColor);
        }
    }
}

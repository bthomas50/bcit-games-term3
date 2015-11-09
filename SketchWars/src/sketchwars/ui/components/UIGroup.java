/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.ui.components;

import java.util.ArrayList;
import org.joml.Vector2d;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class UIGroup extends UIComponent {
    private final ArrayList<UIComponent> components;
    
    /**
     * 
     * @param position can be null
     * @param size  can be null
     */
    public UIGroup(Vector2d position, Vector2d size) {
        super(position, size, null, true);
        
        components = new ArrayList<>();
    }
    
    public void addUIComponent(UIComponent component) {
        if (component != null) {
            components.add(component);
        }
    }
    
    public void removeUIComponent(UIComponent component) {
        if (component != null) {
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
    
    @Override
    public void redraw() { 
        
    }
}

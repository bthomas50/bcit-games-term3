/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.ui.components;

import java.util.ArrayList;
import org.joml.Vector2d;
import sketchwars.graphics.Texture;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class ListBox extends UIComponent implements UIActionListener {
    private float ITEM_SCALE_X = 0.95f;
    
    private final ArrayList<Label> items;
    
    private float itemHeight;
    private Label selection;
    private final Texture selectionBG;
    
    public ListBox(Vector2d position, Vector2d size, float itemHeight, Texture background) {
        super(position, size, background, true);
        
        this.itemHeight = itemHeight;
        
        if (background == null) {
            setBackground(Texture.loadTexture("content/uicomponents/textbox.png", false));
        }
        
        items = new ArrayList<>();
        
        selectionBG = Texture.loadTexture("content/uicomponents/selection.png", false);
    }

    public void setItemHeight(float itemHeight) {
        if (itemHeight > 0) {
            this.itemHeight = itemHeight;

            int total = items.size();
            for (int i = 0; i < total; i++) {
                Label item = items.get(i);
                
                if (item != null) {
                    item.setSize(new Vector2d(size.x * ITEM_SCALE_X, itemHeight));
                }
            }
        }
    }
    
    public void addItem(String item) {
        if (item != null) {
            Vector2d lblSize = new Vector2d(size.x * ITEM_SCALE_X, itemHeight);
            Label label = new Label(item, font, position, lblSize, null);
            
            label.setHandleInput(true);
            label.addActionListener(this);
                    
            items.add(label);
        }
    }
    
    public String getSelectedItem() {
        if (selection != null) {
            return selection.getText();
        }
        return null;
    }
    
    private void deselectAll() {
        selection = null;
        int total = items.size();
        for (int i = 0; i < total; i++) {
            Label item = items.get(i);

            if (item != null) {
                item.setBackground(null);
            }
        }
    }

    @Override
    public void render() {
        renderBackground();
        
        int total = items.size();
        for (int i = 0; i < total; i++) {
            Label item = items.get(i);

            if (item != null) {
                double top = position.y + size.y/2.0;
                double yCenter = (top - (itemHeight * i)) - itemHeight/2.0;
                Vector2d itemPosition = new Vector2d(position.x, yCenter);
                item.setPosition(itemPosition);
                
                if (selection != null && selection.equals(item)) {
                    selectionBG.draw(null, (float)item.position.x, (float)item.position.y, (float)size.x, itemHeight);
                }
                item.render();
            }
        }
        
        update();
    }

    @Override
    public void action(UIComponent component) {
        if (component != null) {
            if (component instanceof Label) {
                deselectAll();
                System.out.println("fffff");
                selection = (Label)component;
            } else if (component instanceof Button) {
                
            }
        }
    }
}

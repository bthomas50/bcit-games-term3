/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.ui.components;

import org.joml.Vector2d;
import sketchwars.graphics.Texture;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class ComboBox extends UIComponent implements UIActionListener {
    private static final float BUTTON_SCALE_X = 0.2f;
    private static final float LISTBOX_SCALE_Y = 3;
    
    private final Label textbox; 
    private ListBox listBox;
    private final Button dropButton;
    
    public ComboBox(Vector2d position, Vector2d size, Texture background) {
        super(position, size, background, true);
        listBox = new ListBox(new Vector2d(), new Vector2d(), (float) size.y, background);
        listBox.setVisible(false);
        
        if (background == null) {
            Texture texture = Texture.loadTexture("content/uicomponents/textbox.png", false);
            setBackground(texture);
            listBox.setBackground(texture);
        }
        
        Texture btnTex = Texture.loadTexture("content/uicomponents/scrollbtn_normal.png", false);
        Texture btnOverTex = Texture.loadTexture("content/uicomponents/scrollbtn_over.png", false);
        Texture btnPressTex = Texture.loadTexture("content/uicomponents/scrollbtn_press.png", false);
        dropButton = new Button(new Vector2d(), new Vector2d(), btnTex, btnPressTex, btnOverTex);
        
        textbox = new Label("", null, position, size, null);
        
        listBox.addActionListener((ComboBox)this);
        dropButton.addActionListener((ComboBox)this);
        addActionListener((ComboBox)this);
        
        updateSubComponents();
    }
    
    public void setSelection(int index) {
        if (listBox.setSelection(index)) {
            textbox.setText(listBox.getSelectedItem());
        }
    }

    public ListBox getListBox() {
        return listBox;
    }

    public void setListBox(ListBox newListBox) {
        listBox = newListBox;
        newListBox.setVisible(false);
        newListBox.setHandleInput(true);
        newListBox.addActionListener(this);
        
        updateSubComponents();
    }
    
    public void addItem(String item) {
        listBox.addItem(item);
        updateSubComponents();
    }
    
    public void removeItem(String item) {
        listBox.removeItem(item);
        updateSubComponents();
    }
    
    public String getSelectedItem() {
       return listBox.getSelectedItem();
    }
    
    @Override
    public void render() {
        if (visible) {
            listBox.render();
            
            super.render();
            textbox.render();
            dropButton.render();
        }
        
        
        if (!selected) {
            hideListBox();
        }
    }
    
    public void hideListBox() {
        listBox.setVisible(false);
    }
    
    private void updateSubComponents() {
        textbox.setPosition(position);
        textbox.setSize(size);
        textbox.setFont(font);
        
        Vector2d listboxSize = new Vector2d(size.x, size.y * LISTBOX_SCALE_Y);
        Vector2d listboxPos = new Vector2d(position.x, position.y - size.y * 2);
        listBox.setPosition(listboxPos);
        listBox.setSize(listboxSize);
        listBox.setFont(font);
        
        Vector2d btnSize = new Vector2d(size.x * BUTTON_SCALE_X, size.y);
        float btnX = (float) (position.x + (size.x - btnSize.x)/2.0);
        Vector2d btnPos = new Vector2d(btnX, position.y);
        dropButton.setPosition(btnPos);
        dropButton.setSize(btnSize);
    }

    @Override
    public void redraw() {
        updateSubComponents();
    }

    @Override
    public void action(UIComponent component, float x, float y) {
        if (component != null) {
            if (component instanceof ListBox) {
                ListBox box = (ListBox) component;
                
                if (!box.isOnScrollButtons(x, y)) {
                    textbox.setText(box.getSelectedItem());
                    toggleDropDownMenu();
                }
            } else if (component instanceof ComboBox &&
                !dropButton.contains(x, y) && !dropButton.contains(x, y))  {
                toggleDropDownMenu();
            } else if (component instanceof Button) {
                toggleDropDownMenu();
            }
        }
    }

    private void toggleDropDownMenu() {
        listBox.setVisible(!listBox.isVisible());
        
        if (listBox.isVisible()) {
            listBox.redraw();
        }
    }
}

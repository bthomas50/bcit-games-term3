/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.ui.components;

import java.awt.Color;
import java.util.ArrayList;
import org.joml.Vector2d;
import sketchwars.graphics.Texture;
import sketchwars.input.DWheelState;
import sketchwars.input.MouseHandler;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class ListBox extends UIComponent implements UIActionListener {
    private static final float SCROLL_RATE_BTN = 10;
    
    private static final float SCROLL_RATE = 0.004f;
    private static final float ITEM_SCALE_X = 0.95f;
    private static final float PADDING_Y = 0.05f;
    
    private static final float BTN_SCALE_X = 0.15f;
    private static final float BTN_SCALE_Y = 0.2f;
    
    private final ArrayList<Label> items;
    
    private float itemHeight;
    private Label selection;
    private Texture selectionBG;
      
    private float scrollPosition;
    private final Button scrollDown;
    private final Button scrollUp;
    
    public ListBox(Vector2d position, Vector2d size, float itemHeight, Texture background) {
        super(position, size, background, true);
        
        this.itemHeight = itemHeight;
        
        if (background == null) {
            setBackground(Texture.loadTexture("content/uicomponents/textbox.png", false));
        }
        
        items = new ArrayList<>();
        
        selectionBG = Texture.loadTexture("content/uicomponents/selection.png", false);
        
        Texture btnTex = Texture.loadTexture("content/uicomponents/scrollbtn_normal.png", false);
        Texture btnOverTex = Texture.loadTexture("content/uicomponents/scrollbtn_over.png", false);
        Texture btnPressTex = Texture.loadTexture("content/uicomponents/scrollbtn_press.png", false);
        
        scrollDown = new Button(new Vector2d(), new Vector2d(), btnTex, btnPressTex, btnOverTex);
        scrollUp = new Button(new Vector2d(), new Vector2d(), btnTex, btnPressTex, btnOverTex);
        
        scrollDown.addActionListener((ListBox)this);
        scrollUp.addActionListener((ListBox)this);
    }

    public Button getScrollUpButton() {
        return scrollUp;
    }
    
    public Button getScrollDownButton() {
        return scrollDown;
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
                    
            setLabelPosition(label, items.size());
            label.setFont(font);
            label.setFontColor(fontColor);
            items.add(label);
        }
    }
    
    /**
     * remove the first occurrence of the the given item
     * @param item 
     */
    public void removeItem(String item) {
        if (item != null) {
            for (Label lbl: items) {
                if (lbl.getText().equals(item)) {
                    items.remove(lbl);
                    break;
                }
            }
        }
    }
    
    /**
     * remove all items
     */
    public void clearAll() {
        items.clear();
    }
    
    public String getSelectedItem() {
        if (selection != null) {
            return selection.getText();
        }
        return null;
    }
    
    @Override
    public void render() {
        if (visible) {
            renderBackground();

            int total = items.size();
            for (int i = 0; i < total; i++) {
                Label item = items.get(i);

                if (item != null) {
                    if (contains(item)) {
                        if (selection != null && selection.equals(item)) {
                            selectionBG.draw(null, (float)item.position.x, (float)item.position.y, (float)size.x, itemHeight);
                        }
                        item.render();
                    } else if (containsSomeParts(item)) {
                        handlePartialLabelRender(item);
                    }
                }
            }

            renderButtons();

            update();
        }
    }
    
    public void setSelectionBackground(Texture texture) {
        if (texture != null && texture.getTextureID() != -1) {
            this.selectionBG = texture;
        }
    }
    
    public void setSelectionBackgroundColor(Color color) {
        if (color != null) {
            int bgWidth = (int) selectionBG.getTextureWidth();
            int bgHeight = (int) selectionBG.getTextureHeight();
            Texture newSelectionBG = Texture.createTextureFromColor(color, bgWidth, bgHeight, false);
            
            if (newSelectionBG != null) {
                this.selectionBG = newSelectionBG;
            }
        }
    }

    @Override
    public void update() {
        super.update();
        
        if (mouseInComponent) {
            handleInput();
        }
    }
    

    private void handlePartialLabelRender(Label label) {
        Texture lblTexure = label.getLabel();
        
        double boxTop = getTop();
        double boxBottom = getBottom();
        double itemTop = label.getTop();
        double itemBottom = label.getBottom();

        Vector2d textCoords[] = Texture.generateTextureCoordinates();

        float paddingY = (float) (itemHeight * PADDING_Y);
        float lblHeight = itemHeight;
       
        float renderY = 0;
        
        if (itemTop >= boxTop) {
            float difference = (float)(itemTop - boxTop);
            float differencePercentage = (difference/itemHeight);
            lblHeight = (float)((itemHeight * (1 - differencePercentage)));
            
            renderY = (float)boxTop - ((itemHeight - difference)/2.0f) - paddingY;
            
            Vector2d topLeft = textCoords[0];
            Vector2d topRight = textCoords[3];
            topLeft.y = differencePercentage;
            topRight.y = topLeft.y;
            
        } else if (itemBottom <= boxBottom) {
            float difference = (float)(boxBottom - itemBottom);
            float differencePercentage = 1 - (difference/itemHeight);
            lblHeight = (float)((itemHeight * differencePercentage));
            
            renderY = (float)boxBottom + ((itemHeight - difference)/2.0f) + paddingY;
            
            Vector2d bottomLeft = textCoords[1];
            Vector2d bottomRight = textCoords[2];
            bottomLeft.y = (bottomLeft.y * differencePercentage);
            bottomRight.y = bottomLeft.y;
        }
  
        if (selection != null && selection.equals(label)) {
            selectionBG.draw(textCoords, (float)position.x, renderY, (float)size.x, lblHeight);
        }
        
        lblTexure.draw(textCoords, (float)label.position.x, renderY, (float)label.size.x, lblHeight);
        label.update();
    }

    private void setLabelPosition(Label label, int index) {
        double top = position.y + size.y/2.0;
        double yCenter = (top - (itemHeight * index)) - itemHeight/2.0 - 0.02;
        Vector2d itemPosition = new Vector2d(position.x, yCenter);
        label.setPosition(itemPosition);
    }

    private void handleInput() {
        if (canScrollDown()&& MouseHandler.dwheelState == DWheelState.FORWARD) {
            scroll(-SCROLL_RATE, MouseHandler.dWheelValue);
        } else if (canScrollUp()&& MouseHandler.dwheelState == DWheelState.BACKWARD) {
            scroll(SCROLL_RATE, MouseHandler.dWheelValue);
        }
    }

    private void scroll(float rate, float scale) {
        scrollPosition += (rate * scale); 
        updateItemPositions();
    }

    private boolean canScrollUp() {
        int total = items.size();
        
        if (total > 1) {
            Label last = items.get(total - 1);
            
            return last.getBottom() < getBottom();
        }
        
        return false;
    }
    
    private boolean canScrollDown() {
        int total = items.size();
        
        if (total > 1) {
            Label first = items.get(0);
            
            return first.getTop() > getTop();
        }
        
        return false;
    }

    private void renderButtons() {
        float paddingY = (float) (itemHeight * PADDING_Y);
        double btnWidth = size.x * BTN_SCALE_X;
        double btnHeight = size.x * BTN_SCALE_Y;
        double btnPosX = getRight() - btnWidth/2.0;
        double btnUpPosY = getTop() - btnHeight/2.0 - paddingY;
        double btnDownPosY = getBottom() + btnHeight/2.0 + paddingY;
        
        Vector2d btnSize = new Vector2d(btnWidth, btnHeight);
        scrollUp.setSize(btnSize);
        scrollDown.setSize(btnSize);
        
        scrollUp.setPosition(new Vector2d(btnPosX, btnUpPosY));
        scrollDown.setPosition(new Vector2d(btnPosX, btnDownPosY));
        
        scrollUp.render();
        scrollDown.render();
    }

    @Override
    public void redraw() {
        for (Label lbl: items) {
            lbl.setFont(font);
            lbl.setFontColor(fontColor);
        }
        
        updateItemPositions();
    }

    private void updateItemPositions() {
        int total = items.size();
        for (int i = 0; i < total; i++) {
            Label item = items.get(i);

            if (item != null) {
                double top = position.y + size.y/2.0;
                double yCenter = (top - (itemHeight * i)) - itemHeight/2.0 + scrollPosition;
                Vector2d itemPosition = new Vector2d(position.x, yCenter);
                item.setPosition(itemPosition);
            }
        }
    }

    public boolean setSelection(int index) {
        if (index >= 0 && index < items.size()) {
            selection = items.get(index);
            return true;
        }
        
        return false;
    }
    
    public int getSelection() {
        if (selection != null) {
            return items.indexOf(selection);
        }
        
        return -1;
    }

    @Override
    public void action(UIComponent component, float x, float y) {
        if (component != null) {            
            if (component instanceof Label && !isOnScrollButtons(x, y)) {
                selection = (Label)component;
            } else if (component instanceof Button) {
                if (canScrollUp() && component.equals(scrollDown)) {
                    scroll(SCROLL_RATE, SCROLL_RATE_BTN);
                } else if (canScrollDown()&& component.equals(scrollUp)) {
                    scroll(-SCROLL_RATE, SCROLL_RATE_BTN);
                }
            }
        }
    }
    
    boolean isOnScrollButtons(float x, float y) {
        return scrollDown.contains(x, y) || scrollUp.contains(x, y);
    }
}

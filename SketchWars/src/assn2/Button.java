package assn2;

import sketchwars.physics.BoundingBox;
import sketchwars.graphics.*;

public class Button implements GraphicsObject
{
    private BoundingBox bounds;
    private Texture normalTexture;
    private Texture pressedTexture;
    private ButtonClickCommands command;

    public Button(BoundingBox bounds, Texture normal, Texture pressed, ButtonClickCommands command)
    {
        this.bounds = bounds;
        normalTexture = normal;
        pressedTexture = pressed;
        this.command = command;
    }

    public boolean contains(int x, int y) 
    {
        return bounds.contains(x, y);
    }

    public ButtonClickCommands getCommand()
    {
        return command;
    }

    @Override
    public void render()
    {
        normalTexture.drawInScreenCoords(bounds.getLeft(), bounds.getTop(), bounds.getWidth(), bounds.getHeight());
    }
};
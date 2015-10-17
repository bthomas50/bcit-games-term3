package sketchwars.ui;

import sketchwars.physics.BoundingBox;
import sketchwars.graphics.*;
import sketchwars.input.Command;

//a generic button that generates a command of type T
public class Button<T> implements GraphicsObject
{
    private BoundingBox bounds;
    private Texture normalTexture;
    private Texture pressedTexture;
    private T command;

    public Button(BoundingBox bounds, Texture normal, Texture pressed, T commandToGenerate)
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

    public T getCommand()
    {
        return command;
    }

    @Override
    public void render()
    {
        normalTexture.drawInScreenCoords(bounds.getLeft(), bounds.getTop(), bounds.getWidth(), bounds.getHeight());
    }
};
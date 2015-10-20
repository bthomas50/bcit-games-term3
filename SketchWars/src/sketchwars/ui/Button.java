package sketchwars.ui;

import sketchwars.physics.BoundingBox;
import sketchwars.graphics.*;
import sketchwars.input.Command;

//a generic button that generates a command of type T
public class Button<T> implements Drawable
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
        normalTexture.draw(null, bounds.getLeft()/1024.0f, bounds.getTop()/1024.0f, bounds.getWidth()/1024.0f, bounds.getHeight()/1024.0f);
    }

};
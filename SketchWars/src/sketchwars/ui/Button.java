package sketchwars.ui;

import org.joml.Vector2d;
import sketchwars.graphics.*;


//a generic button that generates a command of type T
public class Button<T> implements GraphicsObject
{
    
    private Vector2d position;
    private Vector2d size;
    private Texture normalTexture;
    private Texture pressedTexture;
    private T command;

    public Button(Vector2d position,Vector2d size, Texture normal, Texture pressed, T commandToGenerate)
    {
        this.position = position;
        this.size = size;
        normalTexture = normal;
        pressedTexture = pressed;
        this.command = commandToGenerate;
    }

    public boolean contains(float x, float y) 
    {
        return x > position.x - size.x/2 && x < position.x + size.x/2 && 
               y < position.y + size.y/2 && y > position.y - size.y/2;
    }

    public T getCommand()
    {
        return command;
    }

    @Override
    public void render()
    {
        normalTexture.draw(null, (float)position.x, (float)position.y, (float)size.x, (float)size.y);
    }

    @Override
    public boolean hasExpired() {
        return false;
    }

    @Override
    public String toString() {
        return position.x + ", " + position.y ;
    }

};
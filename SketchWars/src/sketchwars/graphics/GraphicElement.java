/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.graphics;

import org.joml.Vector2d;

/**
 *
 * @author a00762764
 */
public class GraphicElement implements GraphicsObject
{
    private boolean expired;
    private final Vector2d position;
    private final Vector2d size;
    private final Texture texture;


    public GraphicElement(Vector2d position, Vector2d size, Texture texture)
    {
        this.position = position;
        this.size = size;
        this.texture = texture;

    }

    @Override
    public void render()
    {
        texture.draw(null, (float)position.x, (float)position.y, (float)size.x, (float)size.y);
    }

    @Override
    public boolean hasExpired() {
        return expired;
    }

    @Override
    public String toString() {
        return position.x + ", " + position.y ;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}

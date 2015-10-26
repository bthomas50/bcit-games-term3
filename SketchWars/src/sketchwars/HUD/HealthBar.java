
package sketchwars.HUD;

import sketchwars.graphics.*;
import sketchwars.game.GameObject;
import org.joml.Vector2d;

/**
 *
 * @author A00813191
 */
public class HealthBar implements GraphicsObject, GameObject {
    private Texture filledBar;
    private Texture emptyBar;
    private Vector2d position;
    private int currentHealth;
    
    public HealthBar(Texture filled, Texture empty, Vector2d pos)
    {
        filledBar = filled;
        emptyBar = empty;
        position = pos;
        
    }
    @Override
    public void update(double delta)
    {
        //nothing really needed here. Updates to healthbar will be done inside SketchCharacter
    }
    
    @Override
    public void render()
    {
        //draw full empty bar
        //draw % (in the x coord) of filledbar texture equalling the currentHealth
    }
    
    public void setHealth (int health)
    {
        currentHealth = health;
    }
    
    public void setPosition (Vector2d pos)
    {
        position = pos;
    }
    
    @Override
    public boolean hasExpired() {
        return false;
    }
}

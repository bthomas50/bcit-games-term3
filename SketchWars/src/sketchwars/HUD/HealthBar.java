
package sketchwars.HUD;

import sketchwars.graphics.*;
import sketchwars.game.GameObject;
import sketchwars.graphics.Texture;
import sketchwars.physics.Vectors;

/**
 *
 * @author A00813191
 */
public class HealthBar implements GraphicsObject, GameObject {
    private Texture filledBar;
    private Texture emptyBar;
    private long position;
    private float drawPosX, drawPosY;
    private int currentHealth;
    private int maxHealth;
    public static Texture[] lifeBars = new Texture[8];
    
    public HealthBar()
    {
    
    }
    public HealthBar(Texture empty, Texture filled, long pos)
    {
        filledBar = filled;
        emptyBar = empty;
        position = pos;
        
    }
    @Override
    public void update(double delta)
    {
        //should probably add text update here when we got font working
    }
    
    @Override
    public void render()
    {
        emptyBar.draw(null,
                     drawPosX, 
                     drawPosY, 
                     0.1f, 
                     0.025f);
        
        filledBar.draw(null,
                      drawPosX, 
                      drawPosY, 
                      (float)currentHealth / (float)maxHealth * 0.1f, 
                      0.025f);
    }
    
    private double getHealthRatio()
    {
        return (double)currentHealth / (double)maxHealth; 
    }
    
    public void setHealth (int health)
    {
        currentHealth = health;
    }
    
    public void setMaxHealth (int maxHealth)
    {
        this.maxHealth = maxHealth;
    }
    
    public void setPosition (float drawX, float drawY)
    {
        drawPosX = drawX;
        drawPosY = drawY;
    }
    
    @Override
    public boolean hasExpired() {
        return false;
    }
    
    public static void loadTextures()
    {
        lifeBars[0] = Texture.loadTexture("content/misc/redEmpty.png", true);
        lifeBars[1] = Texture.loadTexture("content/misc/redFilled.png", true);
        lifeBars[2] = Texture.loadTexture("content/misc/blueEmpty.png", true);
        lifeBars[3] = Texture.loadTexture("content/misc/blueFilled.png", true);
        lifeBars[4] = Texture.loadTexture("content/misc/yellowEmpty.png", true);
        lifeBars[5] = Texture.loadTexture("content/misc/yellowFilled.png", true);
        lifeBars[6] = Texture.loadTexture("content/misc/greenEmpty.png", true);
        lifeBars[7] = Texture.loadTexture("content/misc/greenFilled.png", true);   
    }
}

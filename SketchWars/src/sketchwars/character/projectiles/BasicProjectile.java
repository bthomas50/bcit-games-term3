package sketchwars.character.projectiles;

import sketchwars.graphics.GraphicsObject;
import sketchwars.graphics.Texture;
import sketchwars.physics.*;
import sketchwars.game.GameObject;
import sketchwars.character.SketchCharacter;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public abstract class BasicProjectile implements GraphicsObject, GameObject{
    protected Collider coll;
    protected Texture texture;
    protected float power;
    protected long direction;
    protected double lifespan; //in milliseconds
    
    protected double elapsed;
    protected boolean isActive;
    
    protected int damage;
    private boolean consumed;
    private SketchCharacter owner; //to prevent taking damage to it self (unless grenade)
    
    private double scale;
    
    public BasicProjectile(Texture texture, double scale) {
        this.texture = texture;
        this.scale = scale;
    }
     
    @Override
    public void update(double elapsedMillis) {
        if (isActive) {
            elapsed += elapsedMillis;
        }
    }

    public void setCollider(Collider coll) {
        this.coll = coll;
    }

    public double getScale() {
        return scale;
    }
    
    @Override
    public void render() {
        if (texture != null) {
            BoundingBox bounds = coll.getBounds();
            long vCenter = bounds.getCenterVector();
            texture.drawNormalized(Vectors.xComp(vCenter) / 1024.0 , Vectors.yComp(vCenter) / 1024.0, scale);
        }
    }
    
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }

    public Collider getCollider() {
        return coll;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public void setDirection(long direction) {
        this.direction = direction;
    }

    public boolean hasExpired() {
        return elapsed > lifespan;
    }

    public void setLifespan(double lifespan) {
        this.lifespan = lifespan;
    }
    
    public void setActive(boolean value) {
        isActive = value;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setExpired(boolean value) {
        if (value) {
            elapsed = lifespan + 1;
        } else {
            elapsed = 0;
        }
    }

    public boolean isConsumed() {
        return consumed;
    }

    public void setConsumed(boolean consumed) {
        this.consumed = consumed;
    }

    public SketchCharacter getOwner() {
        return owner;
    }

    public void setOwner(SketchCharacter owner) {
        this.owner = owner;
    }
}

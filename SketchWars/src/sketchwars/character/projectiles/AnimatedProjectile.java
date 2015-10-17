package sketchwars.character.projectiles;

import sketchwars.animation.*;
import sketchwars.game.*;
import sketchwars.graphics.*;
import sketchwars.physics.*;

public class AnimatedProjectile implements GameObject, GraphicsObject
{
    private Animation animation;
    protected Collider coll;
    private int damage;

    public AnimatedProjectile(Animation anim)
    {
        animation = anim;
    }

    public void setCollider(Collider coll) {
        this.coll = coll;
    }

    @Override
    public void update(double elapsedMillis) 
    {

    }

    @Override
    public boolean hasExpired()
    {
        return false;
    }

    @Override
    public void render()
    {
        animation.render();
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
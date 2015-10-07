package sketchwars.character.projectiles;

import sketchwars.graphics.Texture;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class MeleeProjectile extends BasicProjectile {
    private static final int DAMAGE = 20;
    private static final double OBJECT_RADIUS = 48.0;
    private static final double LIFESPAN = 1000;
    
    private double meleeObjRadius;
    
    public MeleeProjectile(Texture texture) {
        super(texture);
        
        setLifespan(LIFESPAN);
        meleeObjRadius = OBJECT_RADIUS;
        setDamage(DAMAGE);
    }

    public double getMeleeObjRadius() {
        return meleeObjRadius;
    }

    public void setMeleeObjRadius(double meleeObjRadius) {
        this.meleeObjRadius = meleeObjRadius;
    }
}

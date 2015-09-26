package sketchwars.character.projectiles;

import sketchwars.graphics.Texture;
import sketchwars.physics.*;
/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class TestBullet extends AbstractProjectile {

    public TestBullet() {
        coll = new PixelCollider(BitMaskFactory.createRectangle(1, 1));
    }

    @Override
    public void init() {
        texture = new Texture();
        texture.loadTexture("content/char/projectiles/testB.png");
    }
    
}

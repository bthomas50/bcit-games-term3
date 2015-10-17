package sketchwars.animation;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import sketchwars.exceptions.AnimationException;

/**
 * This for the alpha grenade explosion (will re-factor and improve after alpha)
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class Explosion extends Animation {
    private static final int DURATION = 500;
    
    public Explosion() throws IOException, AnimationException {
        super(ImageIO.read(new File("content/animation/explosion.png")), 1, DURATION, false);
    }  
}

package sketchwars.game;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public interface GameObject {
    /**
     * Game object update that provides the frame length in milliseconds
     * @param delta frame length in milliseconds
     */
    void update(double delta);
}

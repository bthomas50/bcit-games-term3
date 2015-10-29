package sketchwars;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 * @author Brian <bthomas50@my.bcit.ca>
 */
public interface Updateable
{
    /**
     * Game object update that provides the frame length in milliseconds
     * @param delta frame length in milliseconds
     */
    void update(double delta);
}
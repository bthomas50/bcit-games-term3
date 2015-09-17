package Physics;


/**
 *
 * @author Brian
 */
public class BoundingBox
{
    private int top;
    private int left;
    private int bottom;
    private int right;
    
    public BoundingBox(long vTopLeft, long vBottomRight)
    {
		
    }
	
    public BoundingBox(int top, int left, int bottom, int right)
    {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }
    
    public int getTop()
    {
        return top;
    }

    public int getLeft()
    {
        return left;
    }

    public int getBottom()
    {
        return bottom;
    }

    public int getRight()
    {
        return right;
    }
	
    public boolean contains(BoundingBox other)
    {
        return (top <= other.top) && 
               (left <= other.left) && 
               (bottom >= other.bottom) && 
               (right >= other.right);
    }
    
    @Override
    public String toString()
    {
        return (top + ", " + left + ", " + bottom + ", " + right + "\n");
    }
}

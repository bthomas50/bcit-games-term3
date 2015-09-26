package sketchwars.physics;

import java.lang.Math;
/**
 * Immutable 2d Axis Aligned Bounding Box for Integer values
 * @author Brian
 */
public class BoundingBox
{
    private final int top;
    private final int left;
    private final int bottom;
    private final int right;
    
    public BoundingBox(long vTopLeft, long vBottomRight)
    {
		top = Vectors.iyComp(vTopLeft);
        left = Vectors.ixComp(vTopLeft);
        bottom = Vectors.iyComp(vBottomRight);
        right = Vectors.ixComp(vBottomRight);
    }
	
    public BoundingBox(int top, int left, int bottom, int right)
    {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }
	
    public boolean contains(BoundingBox other)
    {
        return (top <= other.top) && 
               (left <= other.left) && 
               (bottom >= other.bottom) && 
               (right >= other.right);
    }

    public boolean intersects(BoundingBox other)
    {
        return top <= other.bottom &&
           left <= other.right &&
           bottom >= other.top &&
           right >= other.left;
    }

    public BoundingBox intersection(BoundingBox other)
    {
        if(!intersects(other))
        {
            return null;
        }
        else
        {
            return new BoundingBox(Math.max(top, other.top), Math.max(left, other.left), Math.min(bottom, other.bottom), Math.min(right, other.right));
        }
    }

	public long getTopLeftVector()
	{
		return Vectors.create(left, top);
	}
	
	public long getBottomRightVector()
	{
		return Vectors.create(right, bottom);
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

    public int getWidth()
    {
        return 1 + right - left;
    }

    public int getHeight()
    {
        return 1 + bottom - top;
    }
	
	public BoundingBox getTranslatedBox(long vTranslation)
	{
		return new BoundingBox(Vectors.add(vTranslation, getTopLeftVector()), Vectors.add(vTranslation, getBottomRightVector()));
	}

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof BoundingBox))
        {
            return false;
        }
        if(obj == null)
        {
            return false;
        }
        BoundingBox other = (BoundingBox) obj;
        return top == other.top &&
               left == other.left &&
               bottom == other.bottom &&
               right == other.right;
    }

    @Override
    public int hashCode()
    {
        int hash = 17;
        hash = hash * 31 + top;
        hash = hash * 31 + left;
        hash = hash * 31 + bottom;
        hash = hash * 31 + right;
        return hash;
    }

    public String toString()
    {
        return "(" + top + ", " + left + ", " + bottom + ", " + right + ")"; 
    }
}

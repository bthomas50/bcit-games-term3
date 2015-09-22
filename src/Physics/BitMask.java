package Physics;

public class BitMask
{
    private static final int BITS_PER_LONG = 64;
    private static final long[] LONG_MASKS = new long[BITS_PER_LONG];

    static
    {
        for(int i = 0; i < BITS_PER_LONG; i++)
        {
            LONG_MASKS[BITS_PER_LONG - 1 - i] = 1l << (long)i;
        }
    }

    private long[][] data;
    private long vOffset;
    private BoundingBox bounds;

    BitMask(final long[][] data)
    {
        if(data == null)
        {
            throw new IllegalArgumentException("data cannot be null");
        }
        this.data = data.clone();
        for(int i = 0; i < data.length; i++)
        {
            if(data[i] != null)
            {
                this.data[i] = data[i].clone();
            }
        }
        ensureRectangular();
        bounds = new BoundingBox(0, 0, data.length - 1, data[0].length * BITS_PER_LONG - 1);
        vOffset = Vectors.create(0,0);
    }

    private void ensureRectangular()
    {
        int maxWidth = getMaxWidth();
        padToWidth(maxWidth);
    }

    private int getMaxWidth()
    {
        int maxWidth = 0;
        for(int i = 0; i < data.length; i++)
        {
            if(data[i] != null)
            {
                maxWidth = Math.max(maxWidth, data[i].length);
            }
        }
        return maxWidth;
    }

    private void padToWidth(int width)
    {
        for(int i = 0; i < data.length; i++)
        {
            if(data[i] == null)
            {
                data[i] = new long[width];
            }
            else if(data[i].length < width)
            {
                long[] newRow = new long[width];
                System.arraycopy(data[i], 0, newRow, 0, data[i].length);
            }
        }
    }

    //minimize the bounds of the mask by removing empty rows and columns on the edges.
    public void trim()
    {
        int top = findFirstNonemptyRow(data);
        if(top == -1)
        {
            data = null;
            bounds = null;
            return;
        }
        int bottom = findLastNonemptyRow(data);
        int left = findFirstNonemptyColumn(data);
        int right = findLastNonemptyColumn(data);
        bounds = new BoundingBox(top, left, bottom, right);
    }


    private int findFirstNonemptyRow(long[][] bits)
    {
        for(int i = 0; i < bits.length; i++)
        {
            if(!rowIsEmpty(bits[i]))
            {
                return i;
            }
        }
        return -1;
    }

    private int findLastNonemptyRow(long[][] bits)
    {
        for(int i = bits.length - 1; i >= 0; i--)
        {
            if(!rowIsEmpty(bits[i]))
            {
                return i;
            }
        }
        return -1;
    }

    private boolean rowIsEmpty(long[] row)
    {
        for(int i = 0; i < row.length; i++)
        {
            if(row[i] != 0l)
            {
                return false;
            }
        }
        return true;
    }

    private int findFirstNonemptyColumn(long[][] bits)
    {
        int min = -1;
        for(int i = 0; i < bits.length; i++)
        {
            int test = findFirstInRow(bits[i]);
            if(min == -1)
            {
                min = test;
            }
            else if(test != -1)
            {
                min = Math.min(test, min);
            }
        }
        return min;
    }

    private int findFirstInRow(long[] row)
    {
        for(int i = 0; i < row.length; i++)
        {
            int numLeadingZeros = (int) Long.numberOfLeadingZeros(row[i]);
            if(numLeadingZeros != BITS_PER_LONG)
            {
                return (i * BITS_PER_LONG) + numLeadingZeros;
            }
        }
        return -1;
    }

    private int findLastNonemptyColumn(long[][] bits)
    {
        int max = -1;
        for(int i = 0; i < bits.length; i++)
        {
            int test = findLastInRow(bits[i]);
            if(max == -1)
            {
                max = test;
            }
            else if(test != -1)
            {
                max = Math.max(test, max);
            }
        }
        return max;
    }

    private int findLastInRow(long[] row)
    {
        for(int i = row.length - 1; i >= 0; i--)
        {
            int numTrailingZeros = (int) Long.numberOfTrailingZeros(row[i]);
            if(numTrailingZeros != BITS_PER_LONG)
            {
                return (i * BITS_PER_LONG) + (BITS_PER_LONG - 1 - numTrailingZeros);
            }
        }
        return -1;
    }

    public int getWidth()
    {
        return bounds.getWidth();
    }

    public int getHeight()
    {
        return bounds.getHeight();
    }

    public int getArea()
    {
        return getWidth() * getHeight();
    }

    //returns a vector
    public long getCenterOfMass()
    {
        double xCOM = 0.0;
        double yCOM = 0.0;
        int numPoints = 0;
        for(int i = bounds.getTop(); i <= bounds.getBottom(); i++)
        {
            for(int j = bounds.getLeft(); j <= bounds.getRight(); j++)
            {
                final int jOuterIdx = j / BITS_PER_LONG;
                final int jBitIdx = j % BITS_PER_LONG;
                if((data[i][jOuterIdx] & LONG_MASKS[jBitIdx]) != 0l)
                {
                    yCOM += i;
                    xCOM += j;
                    numPoints++;
                }
            }
        }
        xCOM /= numPoints;
        yCOM /= numPoints;
        return Vectors.create(xCOM, yCOM);
    }

    public BoundingBox getBounds()
    {
        return bounds;
    }
}
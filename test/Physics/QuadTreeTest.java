package Physics;

import static org.junit.Assert.*;
import org.junit.Test;

public class QuadTreeTest
{
    @Test
    public void testCreate()
    {
        QuadTree qt = new QuadTree(new BoundingBox(0, 0, 10, 10), 5, 2);
    }
}
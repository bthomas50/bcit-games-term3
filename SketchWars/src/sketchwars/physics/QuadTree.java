package sketchwars.physics;


import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;


/**
 *
 * @author Brian
 */
public class QuadTree
{
    public static final int NUM_CHILDREN = 4;
    private static final int DEFAULT_MAX_OBJ = 10;
    private static final int DEFAULT_MIN_SZ = 32;
	private class QuadTreeControl
	{
		private QuadTree rootNode;
		private int maxObj;
		private int minSz;
		private HashMap<BoundingBox, QuadTree> boxLocations;

		private QuadTreeControl(QuadTree rootNode, int maxObj, int minSz)
		{
			this.rootNode = rootNode;
			this.maxObj = maxObj;
			this.minSz = minSz;
			this.boxLocations = new HashMap<>();
		}
	}
	
    private ArrayList<BoundingBox> objects;
    private BoundingBox bounds;
    private QuadTree children[];
    private QuadTree parent;
	private QuadTreeControl control;
    
    //constructor using the default settings
	public QuadTree(BoundingBox bounds)
	{
        this(bounds, DEFAULT_MAX_OBJ, DEFAULT_MIN_SZ);
	}
    //constructor using user-defined settings
    public QuadTree(BoundingBox bounds, int maxObjects, int minSize)
    {
        this.bounds = bounds;
        this.control = new QuadTreeControl(this, maxObjects, minSize);
        this.objects = new ArrayList<>(control.maxObj);
        this.children = new QuadTree[NUM_CHILDREN];
    }
    //constructor for creating a child tree
	private QuadTree(BoundingBox bounds, QuadTree parent)
    {
        this.bounds = bounds;
        this.control = parent.control;
        this.parent = parent;
        this.objects = new ArrayList<>(control.maxObj);
        this.children = new QuadTree[NUM_CHILDREN];
    }
	
    public int count()
    {
        if(this == control.rootNode)
        {
            return control.boxLocations.size();
        }
        else if(hasSplit())
        {
            return objects.size() + children[0].count() + children[1].count() + children[2].count() + children[3].count();
        }
        return objects.size();
    }
    
    public void prune()
    {
        if(count() <= control.maxObj / 3.0)
        {
            merge();
        }
        else if(hasSplit())
        {
            for(int i = 0; i < NUM_CHILDREN; i++)
            {
                children[i].prune();
            }
        }
    }
    
    public void clear()
    {
        removeAllObjects();
        for(int i = 0; i < NUM_CHILDREN; i++)
        {
            if(children[i] != null)
            {
                children[i].clear();
                children[i] = null;
            }
        }
    }
    private void removeAllObjects()
    {
        for(BoundingBox bb : objects)
        {
            control.boxLocations.remove(bb);
        }
        objects.clear();
    }
    public boolean insert(BoundingBox box)
    {
        if(!bounds.contains(box))
        {
            return false;
        }
        if(hasSplit())
        {
            //insert into a child, if possible.
            int index = getIndex(box);
            if(index != -1)
            {
                children[index].insert(box);
                return true;
            }
        }
        //can't insert into child. Put into itself.
        objects.add(box);
		control.boxLocations.put(box, this);
        //split if too many objects and size is not too small and we haven't split already.
        if(shouldSplit())
        {
            split();
        }
        return true;
    }
    
	public void remove(BoundingBox box)
	{
        if(!contains(box))
        {
            return;
        }
		QuadTree someTree = control.boxLocations.get(box);
		control.boxLocations.remove(box);
		someTree.objects.remove(box);
	}
	
    public boolean contains(BoundingBox box)
    {
        return control.boxLocations.containsKey(box);
    }

    public boolean replace(BoundingBox oldBox, BoundingBox newBox)
    {
        //try to remove old box.
        if(!contains(oldBox))
        {
            return false;
        }
        else
        {
            remove(oldBox);
            return insertUpwards(newBox);
        }
    }
    
    public List<BoundingBox> retrieve(BoundingBox box)
    {
        ArrayList<BoundingBox> ret = new ArrayList<>();
        if(hasSplit())
        {
            int index = getIndex(box);
            if(index != -1)
            {
                children[index].retrieve(ret, box);
            }
            else
            {
                for(int i = 0; i < children.length; i++)
                {
                    children[i].retrieve(ret, box);
                }
            }
        }
        ret.addAll(objects);
        return ret;
    }
    
    private ArrayList<BoundingBox> retrieve(ArrayList<BoundingBox> list, BoundingBox box)
    {
        if(hasSplit())
        {
            int index = getIndex(box);
            if(index != -1)
            {
                children[index].retrieve(list, box);
            }
            else
            {
                for(int i = 0; i < children.length; i++)
                {
                    children[i].retrieve(list, box);
                }
            }
        }
        list.addAll(objects);
        return list;
    }
    
    private void split()
    {
        int xMid = bounds.getLeft() + (bounds.getRight() - bounds.getLeft()) / 2;
        int yMid = bounds.getTop() + (bounds.getBottom() - bounds.getTop()) / 2;
        
        children[0] = new QuadTree(new BoundingBox(bounds.getTop(), xMid,             yMid,               bounds.getRight()),
                                   this);
        children[1] = new QuadTree(new BoundingBox(bounds.getTop(), bounds.getLeft(), yMid,               xMid - 1),
                                   this);
        children[2] = new QuadTree(new BoundingBox(yMid + 1,        bounds.getLeft(), bounds.getBottom(), xMid - 1),
                                   this);
        children[3] = new QuadTree(new BoundingBox(yMid + 1,        xMid,             bounds.getBottom(), bounds.getRight()),
                                   this);
        //insert all objects that will fit into children into children.
        int i = 0;
        while(i < objects.size())
        {
            int index = getIndex(objects.get(i));
            if (index != -1) 
            {
                children[index].insert(objects.remove(i));
            }
            else
            {
                i++;
            }
        }
    }

    private void merge()
    {
        if(!hasSplit())
        {
            return;
        }
        for(int i = 0; i < NUM_CHILDREN; i++)
        {
            children[i].merge();
            objects.addAll(children[i].objects);
            children[i] = null;
        }
        for(BoundingBox bb : objects)
        {
            control.boxLocations.put(bb, this);
        }
    }

    private boolean insertUpwards(BoundingBox box)
    {
        if(bounds.contains(box))
        {
            insert(box);
            return true;
        }
        if(parent != null)
        {
            return parent.insertUpwards(box);
        }
        else
        {
            return false;
        }
    }

    private int getIndex(BoundingBox box)
    {
        int index = -1;
        for(int i = 0; i < NUM_CHILDREN; i++)
        {
            if(children[i].bounds.contains(box))
            {
                return i;
            }
        }
        return index;
    }
    

	private boolean shouldSplit()
	{
		return isFull() && 
			   isBigEnoughToSplit() && 
               !hasSplit();
	}
	
	private boolean isFull()
	{
		return (objects.size() > control.maxObj);
	}
	
	private boolean isBigEnoughToSplit()
	{
		return (bounds.getBottom() - bounds.getTop() > control.minSz) && 
			   (bounds.getRight() - bounds.getLeft() > control.minSz);
	}
	
	private boolean hasSplit()
	{
		return children[0] != null;
	}
	
    public void print()
    {
        System.out.println("Bounds: " + bounds);
        System.out.println("Objects: \n" + objects);
        if(children[0] != null)
        {
            children[0].print();
            children[1].print();
            children[2].print();
            children[3].print();
        }
    }
}

package Physics;


import java.util.ArrayList;
import java.util.HashMap;


/**
 *
 * @author Brian
 */
public class QuadTree
{
	private class QuadTreeControl
	{
		private QuadTree rootNode;
		private int maxObj;
		private int minSz;
		private HashMap<BoundingBox, QuadTree> boxLocations;

		private QuadTreeControl(QuadTree rootNode)
		{
			//10 objects per node before split, min size 32
			this(rootNode, 10, 32);
		}
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
    
	public QuadTree(BoundingBox bounds)
	{
        this.objects = new ArrayList<>(control.maxObj);
        this.bounds = bounds;
        this.children = new QuadTree[4];
		this.control = new QuadTreeControl(this);
	}
	
    public QuadTree(BoundingBox bounds, int maxObjects, int minSize)
    {
        this.objects = new ArrayList<>(control.maxObj);
        this.bounds = bounds;
        this.children = new QuadTree[4];
		this.control = new QuadTreeControl(this, maxObjects, minSize);
    }
    //constructor for creating a child tree
	private QuadTree(BoundingBox bounds, QuadTree parent)
    {
        this.objects = new ArrayList<>(control.maxObj);
        this.bounds = bounds;
        this.children = new QuadTree[4];
        this.parent = parent;
		this.control = parent.control;
    }
	
    public int count()
    {
        if(children[0] != null)
        {
            return objects.size() + children[0].count() + children[1].count() + children[2].count() + children[3].count();
        }
        return objects.size();
    }
    
    private void merge()
    {
        if(children[0] == null)
        {
            return;
        }
        children[0].merge();
        children[1].merge();
        children[2].merge();
        children[3].merge();
        objects.addAll(children[0].objects);
        objects.addAll(children[1].objects);
        objects.addAll(children[2].objects);
        objects.addAll(children[3].objects);
        for(BoundingBox bb : objects)
        {
            control.boxLocations.put(bb, this);
        }
        children[0] = null;
        children[1] = null;
        children[2] = null;
        children[3] = null;
    }
    
    public void prune()
    {
        if(count() <= 3)
        {
            merge();
        }
        else if(children[0] != null)
        {
            children[0].prune();
            children[1].prune();
            children[2].prune();
            children[3].prune();
        }
    }
    
    public void clear()
    {
        objects.clear();
        for(int i = 0; i < children.length; i++)
        {
            if(children[i] != null)
            {
                children[i].clear();
                children[i] = null;
            }
        }
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
    
    private int getIndex(BoundingBox box)
    {
        int index = -1;
        for(int i = 0; i < children.length; i++)
        {
            if(children[i].bounds.contains(box))
            {
                return i;
            }
        }
        return index;
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
    
    public boolean tryRemove(BoundingBox box)
    {
        if(control.boxLocations.containsKey(box))
		{
			remove(box);
			return true;
		}
		else
		{
			return false;
		}
    }
    
	public void remove(BoundingBox box)
	{
		QuadTree someTree = control.boxLocations.get(box);
		control.boxLocations.remove(box);
		someTree.objects.remove(box);
	}
	
    public boolean replace(BoundingBox oldBox, BoundingBox newBox)
    {
        //try to remove old box.
        if(!tryRemove(oldBox))
        {
            return false;
        }
        //insert new box.
        return tryInsert(newBox);
    }
    
    private boolean tryInsert(BoundingBox box)
    {
        if(bounds.contains(box))
        {
            insert(box);
            return true;
        }
        if(parent != null)
        {
            return parent.tryInsert(box);
        }
        else
        {
            return false;
        }
    }
    
    public ArrayList<BoundingBox> retrieve(BoundingBox box)
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

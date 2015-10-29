package sketchwars.game;

import java.util.ArrayList;
import sketchwars.map.TestMap;

/**
 * @author Najash Najimudeen <najash.najm@gmail.com>
 * @author Brian Thomas <bthomas50@my,bcit.ca>
 * @author David Ly <ly_nekros@hotmail.com>
 */
public class World {
    private ArrayList<GameObject> toAdd;
    protected ArrayList<GameObject> allObjects;
    
    public World() {
        toAdd = new ArrayList<>();
        allObjects = new ArrayList<>();
    }

    public final void addGameObject(GameObject obj) {
        toAdd.add(obj);
    }

    public void update(double deltaMillis) {
        addPendingObjects();
        updateObjects(deltaMillis);
        removeExpiredObjects();
    }
    
    protected final void addPendingObjects() {
        for(GameObject obj : toAdd) {
            allObjects.add(obj);
        }
        toAdd.clear();
    }

    protected final void updateObjects(double deltaMillis) {
        for(GameObject obj : allObjects) {
            obj.update(deltaMillis);
        }
    }

    protected final void removeExpiredObjects() {
        ArrayList<GameObject> toDelete = new ArrayList<>();
        for(GameObject obj : allObjects) {
            if(obj.hasExpired()) {
                toDelete.add(obj);
            }
        }
        for(GameObject deleting : toDelete) {
            allObjects.remove(deleting);
        }
    }

    public void clear() {
        allObjects.clear();
    }
}

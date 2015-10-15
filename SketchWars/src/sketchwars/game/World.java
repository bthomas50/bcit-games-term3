package sketchwars.game;

import java.util.ArrayList;

/**
 * @author Najash Najimudeen <najash.najm@gmail.com>
 * @author Brian Thomas <bthomas50@my,bcit.ca>
 * @author David Ly <ly_nekros@hotmail.com>
 */
public class World {
    protected ArrayList<GameObject> allObjects;
    
    public World() {
        allObjects = new ArrayList<>();
    }

    public final void addGameObject(GameObject obj) {
        allObjects.add(obj);
    }

    public void update(double deltaMillis) {
        updateObjects(deltaMillis);
    }
    
    protected final void updateObjects(double deltaMillis) {
        for(GameObject obj : allObjects) {
            obj.update(deltaMillis);
        }
    }

    public void clear() {
        allObjects.clear();
    }
}

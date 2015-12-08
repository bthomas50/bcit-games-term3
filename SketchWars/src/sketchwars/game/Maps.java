package sketchwars.game;

import java.io.Serializable;

/**
 *
 * @author a00861166
 */
public enum Maps implements Serializable{
    NORMAL("content/map/BiggerMap.png");
    
    public final String path;

    Maps(String p) {
        path = p;
    }
}

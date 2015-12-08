package sketchwars.game;

import java.io.Serializable;

/**
 *
 * @author a00861166
 */
public enum Maps implements Serializable{
    NORMAL("content/map/BiggerMap.png", 
            "content/map/new_sky.jpg", 
            "content/shader/2d_water/water.png"),
    
    SALMAN("content/map/salman_level.png", 
            "content/map/new_sky.jpg", 
            "content/shader/2d_water/water.png");
    
    public final String path;
    public final String foregroundPath;
    public final String waterPath;
    
    Maps(String path, String foregroundPath, String waterPath) {
        this.path = path;
        this.foregroundPath = foregroundPath;
        this.waterPath = waterPath;
    }
}

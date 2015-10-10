package sketchwars.scenes;

import com.google.common.collect.TreeMultiset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;
import sketchwars.exceptions.SceneException;

/**
 * Use this class to create scenes
 * @author Najash Najimudeen <najash.najm@gmail.com>
 * @param <T> layer index type
 */
public class Scene<T> {
    private final HashMap<T, Layer> layerIndex;
    private final TreeMultiset<Layer> drawableLayers;

    public Scene() {
        layerIndex = new HashMap<>();
        drawableLayers = TreeMultiset.create();
    }
    
    /**
     * Sets a default z-order depending on the order it is added
     * @param index
     * @param layer 
     */
    public void addLayer(T index, Layer layer) {
        drawableLayers.add(layer);
        layerIndex.put(index, layer);
    }
    
    /**
     * remove a layer (removeLayerByIndex is better)
     * @param layer
     * @return 
     */
    public boolean removeLayer(Layer layer) {
        if (layerIndex.containsValue(layer)) {
            drawableLayers.remove(layer);
            
            return layerIndex.values().remove(layer);
        }
        return false;
    }
    
    /**
     * remove a layer using its index
     * @param index
     * @return 
     */
    public boolean removeLayerByIndex(T index) {
        if (layerIndex.containsKey(index)) {
            Layer layer = layerIndex.get(index);
            drawableLayers.remove(layer);
            
            return (layerIndex.remove(index) != null);
        }
        return false;
    }
        
    public void render() {
        for (Layer layer : drawableLayers) {
            layer.render();
        }
    }
    
    public void update(double delta) {
        for (Layer layer : drawableLayers) {
            layer.update(delta);
        }
    }

    public Layer getLayer(T index) throws SceneException {
        if (layerIndex.containsKey(index)) {
            return layerIndex.get(index);
        } else  {
            throw new SceneException("Given layer does not exist.");
        }
    }
}

package sketchwars.input;

import java.util.HashMap;
import java.util.Map;
import network.Peer;

/**
 *
 * @author a00861166
 */
public class MultiInputSource implements InputSource {
    private static final int WINDOW_SIZE = 4;
    private final Peer networkInterface;
    private int frameNum = 0;
    
    public MultiInputSource(Peer peer) {
        networkInterface = peer;
    }
    @Override
    public Map<Integer, Input> getCurrentInputs() {
        Input.update();
        Input.handleGameInput();
        networkInterface.broadcastInput(frameNum);
        Map<Integer, Input> allInputs = new HashMap<>();
        if(frameNum > WINDOW_SIZE)
        {
            allInputs = networkInterface.getInputs(frameNum - WINDOW_SIZE);
        }
        
        frameNum++;
        return allInputs;
    }
    
}

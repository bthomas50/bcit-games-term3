/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.input;

import java.util.Map;
import network.Peer;

/**
 *
 * @author a00861166
 */
public class MultiInputSource implements InputSource {
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
        Map<Integer, Input> allInputs = networkInterface.getInputs(frameNum);
        frameNum++;
        return allInputs;
    }
    
}

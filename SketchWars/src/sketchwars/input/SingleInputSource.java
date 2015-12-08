package sketchwars.input;

import java.util.HashMap;
import java.util.Map;

/**
 * Input Source for single player games - just gets the local inputs and assigns them to player 1.
 * @author a00861166
 */
public class SingleInputSource implements InputSource {
    @Override
    public Map<Integer, Input> getCurrentInputs() {
        Input.update();
        Input.handleGameInput();
        Map<Integer, Input> ret = new HashMap<>();
        ret.put(0, Input.currentInput);
        return ret;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.input;

import java.io.IOException;
import java.util.Map;

/**
 *
 * @author a00861166
 */
public interface InputSource {
    Map<Integer, Input> getCurrentInputs() throws IOException;
}

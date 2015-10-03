/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public interface GameObject {
    /**
     * Game object update that provides the frame length in milliseconds
     * @param delta frame length in milliseconds
     */
    void update(double delta);
}

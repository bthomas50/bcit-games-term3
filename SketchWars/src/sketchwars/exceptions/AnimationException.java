/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.exceptions;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class AnimationException extends Exception {

    public AnimationException(final String msg) {
        super("Animation Exception: " + msg);
    }
}

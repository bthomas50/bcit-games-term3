/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.graphics;

import java.nio.ByteBuffer;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class ImageBuffer {
    public ByteBuffer buf;
    public int textureWidth;
    public int textureHeight;
    public int textureID;

    public ImageBuffer() {
        
    }
    
    public ImageBuffer(ByteBuffer buf, int textureWidth, int textureHeight) {
        this.buf = buf;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }
}

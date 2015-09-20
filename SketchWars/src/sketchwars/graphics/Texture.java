package sketchwars.graphics;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import sketchwars.OpenGL;

/**
 * Texture class is capable of loading and rendering images
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class Texture {
    private int textureID;
    private int tWidth;
    private int tHeight;
        
    public Texture() {
        tWidth = 0;
        tHeight = 0;
    }
    
    public int getTextureWidth() {
        return tWidth;
    }
    
    public int getTextureHeight() {
        return tHeight;
    }
    
    public int getTextureID() {
        return textureID;
    }
    
    /**
     * Load a texture into vram (PNG files for now)
     * @param pngFile texture path (PNG files for now)
     * @param textureUnit choose a openGL texture bank (ex:- GL13.GL_TEXTURE0)
     * @return returns the texture ID (negative value indicates error loading texture)
     */
    public int loadTexture(final String pngFile, int textureUnit) {        
        try {
            ByteBuffer buf = loadPNGFile(pngFile);
            // Create a new texture object in memory and bind it
            textureID = GL11.glGenTextures();
            GL13.glActiveTexture(textureUnit);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

            // All RGB bytes are aligned to each other and each component is 1 byte
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

            // Upload the texture data and generate mip maps (for scaling)
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, tWidth, tHeight, 0, 
            GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

            return textureID;
        } catch (IOException ex) {
            Logger.getLogger(Texture.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        textureID = -1;
        return -1;
    }
    
    private ByteBuffer loadPNGFile(final String file) throws IOException {
        ByteBuffer buf;
        InputStream in;
                
        in = new FileInputStream(file);
        // Link the PNG decoder to this stream
        PNGDecoder decoder = new PNGDecoder(in);

        // Get the width and height of the texture
        tWidth = decoder.getWidth();
        tHeight = decoder.getHeight();

        // Decode the PNG file in a ByteBuffer
        buf = ByteBuffer.allocateDirect(
                4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
        buf.flip();

        in.close();
        
        return buf;
    }
    
    /**
     * Draw the texture - coordinates (0, 0) is the middle of the screen
     * @param x X-axis coordinates
     * @param y Y-axis coordinates
     * @param width 'draw width' in pixels
     * @param height 'draw height' in pixels
     */
    public void draw(int x, int y, int width, int height) {
        //convert the position to make sure (0, 0) is the center
        int newX = x + OpenGL.WIDTH/2;
        int newY = y + OpenGL.HEIGHT/2;
        
        draw(newX, newX, newX + width, newX + width,
             newY, newY - height, newY - height, newY);
    }
    
    /**
     * Draw the texture centered - coordinates (0, 0) is the middle of the screen
     * @param x X-axis coordinates
     * @param y Y-axis coordinates
     * @param width 'draw width' in pixels
     * @param height 'draw height' in pixels
     */
    public void drawTextureCentered(int x, int y, int width, int height) {
        //convert the position to make sure (0, 0) is the center
        int newX = x + OpenGL.WIDTH/2 - width/2;
        int newY = y + OpenGL.HEIGHT/2 + height/2;
        
        draw(newX, newX, newX + width, newX + width,
             newY, newY - height, newY - height, newY);
    }
    
    /**
     * Draw the texture centered - coordinates (0, 0) is the middle of the screen
     * @param xP X-axis coordinates (-1 to 1)
     * @param yP Y-axis coordinates (1 to -1)
     * @param widthP 'draw width' percentage of screen width (0 to 1)
     * @param heightP 'draw height' percentage of screen height (0 to 1)
     */
    public void drawNormalized(double xP, double yP, double widthP, double heightP) {
        int width = (int)(OpenGL.WIDTH * widthP);
        int height = (int)(OpenGL.HEIGHT * heightP);

        int x = (int)((OpenGL.WIDTH/2) * xP);
        int y = (int)((OpenGL.HEIGHT/2) * yP);
        
        //convert the position to make sure (0, 0) is the center
        int newX = x + OpenGL.WIDTH/2 - width/2;
        int newY = y + OpenGL.HEIGHT/2 + height/2;
        
        draw(newX, newX, newX + width, newX + width,
             newY, newY - height, newY - height, newY);
    }
    
    /**
     * Draw the texture centered - coordinates (0, 0) is the middle of the screen
     * @param xP X-axis coordinates (-1 to 1)
     * @param yP Y-axis coordinates (1 to -1)
     * @param width 'draw width' in pixels
     * @param height 'draw height' in pixels
     */
    public void drawNormalizedPosition(double xP, double yP, int width, int height) {
        int x = (int)((OpenGL.WIDTH/2) * xP);
        int y = (int)((OpenGL.HEIGHT/2) * yP);
        
        //convert the position to make sure (0, 0) is the center
        int newX = x + OpenGL.WIDTH/2 - width/2;
        int newY = y + OpenGL.HEIGHT/2 + height/2;
        
        draw(newX, newX, newX + width, newX + width,
             newY, newY - height, newY - height, newY);
    }
    
    private void draw(int x1, int x2, int x3, int x4,
                      int y1, int y2, int y3, int y4) {        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

        // Draw a textured quad
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0); GL11.glVertex3f(x1, y1, 0);
        GL11.glTexCoord2f(0, 1); GL11.glVertex3f(x2, y2, 0);
        GL11.glTexCoord2f(1, 1); GL11.glVertex3f(x3, y3, 0);
        GL11.glTexCoord2f(1, 0); GL11.glVertex3f(x4, y4, 0);
        GL11.glEnd();
    }
    
    public void dispose() {
        if (textureID != -1) {
            GL11.glDeleteTextures(textureID);
        }
    }
}

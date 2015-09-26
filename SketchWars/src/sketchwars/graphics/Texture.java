package sketchwars.graphics;

import static org.lwjgl.opengl.GL11.*;
//import com.sun.javafx.geom.Matrix3f;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import sketchwars.OpenGL;

/**
 * Texture class is capable of loading and rendering images
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class Texture {
    private static final HashMap<String, ImageBuffer> textureList = new HashMap<>();
    private static final HashMap<Integer, Integer> textureReference = new HashMap<>();
    
    private int textureID;
    private String path;
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
     * @return returns the texture ID (negative value indicates error loading texture)
     */
    public ImageBuffer loadTexture(final String pngFile) {     
        ImageBuffer imageBuffer;
        
        if (textureList.containsKey(pngFile)) {
            imageBuffer = textureList.get(pngFile);
            
            path = pngFile;
            loadImageBuffer(imageBuffer);
            
            System.out.println("Texture '" + pngFile + "' already exists, using existing reference.");
        } else {
            imageBuffer = loadTextureFromFile(pngFile);
        
            if (imageBuffer == null) {
                reset();
                System.err.println("Error loading texture.");
            } else {
                textureList.put(pngFile, imageBuffer);
                
                path = pngFile;
                loadImageBuffer(imageBuffer);
            }
        }
        
        return imageBuffer;
    }
    
    private void loadImageBuffer(ImageBuffer imageBuffer) {
        incrementReference(imageBuffer.textureID);
        
        textureID = imageBuffer.textureID;
        tWidth = imageBuffer.textureWidth;
        tHeight = imageBuffer.textureHeight;
    }
    
    private static void incrementReference(int textureID) {
        int previous = 0;

        if (textureReference.containsKey(textureID)) {
            previous = textureReference.get(textureID);
        }

        textureReference.put(textureID, previous + 1);
    }
    
    private static void decrementReference(int textureID) {
        int previous = 0;

        if (textureReference.containsKey(textureID)) {
            previous = textureReference.get(textureID);
        }
        
        if (previous > 0) {
            textureReference.put(textureID, previous - 1);
        }
    }
    
    public int getTotalReferences() {
        if (textureID == -1 || textureReference.size() == 0) {
            return 0;
        }
        
        return textureReference.get(textureID);
    }
    
     public static int getTotalReferences(int textureID) {
         return textureReference.get(textureID);
     }
    
    private void reset() {
        path = null;
        textureID = -1;
        tWidth = 0;
        tHeight = 0;
    }

    private static ImageBuffer loadTextureFromFile(final String pngFile) {
        try {
            ImageBuffer imageBuffer = loadPNGFile(pngFile);
            // Create a new texture object in memory and bind it
            imageBuffer.textureID = glGenTextures();
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, imageBuffer.textureID);

            // All RGB bytes are aligned to each other and each component is 1 byte
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

            // Upload the texture data and generate mip maps (for scaling)
            glTexImage2D(GL_TEXTURE_2D,
                    0, GL_RGBA, imageBuffer.textureWidth, imageBuffer.textureHeight, 0, 
            GL_RGBA, GL_UNSIGNED_BYTE, imageBuffer.buf);
            GL30.glGenerateMipmap(GL_TEXTURE_2D);
            
            return imageBuffer;
        } catch (IOException ex) {
            Logger.getLogger(Texture.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }        
    
    private static ImageBuffer loadPNGFile(final String file) throws IOException {
        ByteBuffer buf;
        InputStream in;
                
        in = new FileInputStream(file);
        // Link the PNG decoder to this stream
        PNGDecoder decoder = new PNGDecoder(in);

        // Get the width and height of the texture
        int textureWidth = decoder.getWidth();
        int textureHeight = decoder.getHeight();

        // Decode the PNG file in a ByteBuffer
        buf = ByteBuffer.allocateDirect(
                4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
        buf.flip();

        in.close();
        
        return new ImageBuffer(buf, textureWidth, textureHeight);
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
     * @param scale value by which to scale the original texture
     */
    public void drawNormalized(double xP, double yP, double scale) {
        int width = (int)(tWidth * scale);
        int height = (int)(tHeight * scale);

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
    
   /* public void draw(Matrix3f matrix) {
        glPushMatrix();
        
        
        glTranslatef(matrix.m02, matrix.m12, 0);
        glScaled(matrix.m00, matrix.m11, 0);
        
        drawTextureCentered(0, 0, 10, 10);
        glPopMatrix();
    }*/
    
    private void draw(int x1, int x2, int x3, int x4,
                      int y1, int y2, int y3, int y4) {        
        glBindTexture(GL_TEXTURE_2D, textureID);

        // Draw a textured quad
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0); glVertex3f(x1, y1, 0);
        glTexCoord2f(0, 1); glVertex3f(x2, y2, 0);
        glTexCoord2f(1, 1); glVertex3f(x3, y3, 0);
        glTexCoord2f(1, 0); glVertex3f(x4, y4, 0);
        glEnd();
    }
    
    public void dispose() {
        if (textureID != -1 && getTotalReferences() > 0) {
            decrementReference(textureID);
            
            if (getTotalReferences() == 0) {
                glDeleteTextures(textureID);
            }
        }
        
        reset();
    }
    
    /**
     * Delete all allocated textures
     */
    public static void disposeAllTextures() {
        for (ImageBuffer image : textureList.values()) {
            glDeleteTextures(image.textureID);
        }
        
        textureReference.clear();
        textureList.clear();
    }
    
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        
        if (textureID != -1) {
            dispose();
        }
    }
}
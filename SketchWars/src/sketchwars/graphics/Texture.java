package sketchwars.graphics;

import java.awt.image.BufferedImage;
import org.lwjgl.BufferUtils;
import java.io.File;
import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.joml.Matrix3d;
import org.joml.Vector3d;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import sketchwars.OpenGL;

/**
 * Texture class is capable of loading and rendering images
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class Texture {
    private static final HashMap<String, Texture> textureList = new HashMap<>();
    private static final HashMap<Integer, Integer> textureReference = new HashMap<>();

    private int textureID;
    private float tWidth;
    private float tHeight;
    
    public Texture() {
        tWidth = 0;
        tHeight = 0;
    }
    
    public float getTextureWidth() {
        return tWidth;
    }
    
    public float getTextureHeight() {
        return tHeight;
    }
    
    public int getTextureID() {
        return textureID;
    }
    
    /**
     * Load a texture into vram
     * @param file texture path
     * @return returns the texture ID (negative value indicates error loading texture)
     */
    public static Texture loadTexture(final String file) {
        Texture texture;
        if (textureList.containsKey(file)) {
            texture = textureList.get(file);
            System.out.println("Texture file '" + file + "' previously loaded, using existing texture reference.");
        } else {
            texture = loadTextureFromFile(file);
            
            if (texture != null) {
                System.out.println("Texture file '" + file + "' loaded.");
                textureList.put(file, texture);
            }
        }
        
        if (texture == null) {
            System.err.println("Error loading texture from: " + file);
        } else {            
            incrementReference(texture.getTextureID());
        }
        
        return texture;
    }

    private static void incrementReference(int textureID) {
        int previous = 0;

        if (textureReference.containsKey(textureID)) {
            previous = textureReference.get(textureID);
        } else {
            textureReference.put(textureID, 0);
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
        if (textureID == -1 || textureReference.isEmpty()) {
            return 0;
        }
        
        return textureReference.get(textureID);
    }
    
    public static int getTotalReferences(int textureID) {
        return textureReference.get(textureID);
    }
    
    public static BufferedImage loadImageFile(final String file) throws IOException {
        File imageFile = new File(file);
        return ImageIO.read(imageFile);
    }

    private static Texture loadTextureFromImage(final BufferedImage image) {
        if (image != null) {
            Texture texture = new Texture();
             
            int width = image.getWidth();
            int height = image.getHeight();
            texture.tWidth = width;
            texture.tHeight = height;
            
            ByteBuffer buffer = convertToByteBuffer(image);
            
            // Create a new texture object in memory and bind it
            texture.textureID = glGenTextures();
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture.textureID);

            // All RGB bytes are aligned to each other and each component is 1 byte
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

            // Upload the texture data and generate mip maps (for scaling)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, 
                GL_RGBA, GL_UNSIGNED_BYTE, buffer);
            GL30.glGenerateMipmap(GL_TEXTURE_2D);
            
            return texture;
        }
        return null;
    }

    private static Texture loadTextureFromFile(final String file) {
        try {
            BufferedImage im = loadImageFile(file);
            return loadTextureFromImage(im);
        } catch (IOException ex) {
            Logger.getLogger(Texture.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }    
    
    private static ByteBuffer convertToByteBuffer(BufferedImage image) {
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); //4 for RGBA, 3 for RGB

        for(int y = 0; y < image.getHeight(); y++){
            for(int x = 0; x < image.getWidth(); x++){
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                buffer.put((byte) (pixel & 0xFF));             // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
            }
        }

        buffer.flip();
        return buffer;
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
    
    /**
     * draw the texture using a transformation matrix 
     * @param matrix transformation matrix 
     */
    public void draw(Matrix3d matrix) {   
        glPushMatrix();
        
        //translate so (0, 0) is center of window
        int xCenterOffet = (int)(OpenGL.WIDTH/2);
        int yCenterOffet = (int)(OpenGL.HEIGHT/2);
        glTranslated(xCenterOffet, yCenterOffet, 0);
        
        //quad
        Vector3d point1 = new Vector3d(-0.5, 0.5, 1);
        Vector3d point2 = new Vector3d(0.5, 0.5, 1);
        Vector3d point3 = new Vector3d(0.5, -0.5, 1);
        Vector3d point4 = new Vector3d(-0.5, -0.5, 1);
        
        //tranform quad using the matrix
        point1.mul(matrix);
        point2.mul(matrix);
        point3.mul(matrix);
        point4.mul(matrix);
        
        draw((int)point1.x, (int)point2.x, (int)point3.x, (int)point4.x, 
             (int)point1.y, (int)point2.y, (int)point3.y, (int)point4.y);
        glPopMatrix();
    }
    
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
    }
    
    /**
     * Delete all allocated textures
     */
    public static void disposeAllTextures() {
        for (Texture texture : textureList.values()) {
            glDeleteTextures(texture.textureID);
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
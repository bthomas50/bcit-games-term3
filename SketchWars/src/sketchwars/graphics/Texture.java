package sketchwars.graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import org.lwjgl.BufferUtils;
import java.io.File;
import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import javax.imageio.ImageIO;
import org.joml.Matrix3d;
import org.joml.Vector2d;
import org.joml.Vector3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

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

    /**
     * Load texture from buffered image
     * warning: the texture will not be part of the reference counter
     *          so the same image can be loaded multiple times
     * @param image 
     * @param disableMipMap 
     */
    public Texture(BufferedImage image, boolean disableMipMap) {
        if (image == null) {
            System.err.println("Given image is a null pointer");
        }
        
        Texture texture = loadTextureFromImage(image, disableMipMap);
        textureID = texture.getTextureID();
        tWidth = texture.getTextureWidth();
        tHeight = texture.getTextureHeight();
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
     * @param disableMipMap
     * @return returns the texture ID (negative value indicates error loading texture)
     */
    public static Texture loadTexture(final String file, boolean disableMipMap) {
        Texture texture;
        if (textureList.containsKey(file)) {
            texture = textureList.get(file);
            System.out.println("Texture file '" + file + "' previously loaded, using existing texture reference.");
        } else {
            texture = loadTextureFromFile(file, disableMipMap);
            
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

    private static Texture loadTextureFromImage(final BufferedImage image, boolean disableMipMap) {
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
            
            if (disableMipMap) {
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            } else {
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_NEAREST_MIPMAP_LINEAR, GL11.GL_NEAREST);
            }
            
            GL30.glGenerateMipmap(GL_TEXTURE_2D);
            
            return texture;
        }
        return null;
    }

    private static Texture loadTextureFromFile(final String file, boolean disableMipMap) {
        try {
            BufferedImage im = loadImageFile(file);
            return loadTextureFromImage(im, disableMipMap);
        } catch (IOException ex) {
            System.err.println(file + " : " + ex.getMessage());
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
     * Draw the texture centered - coordinates (0, 0) is the middle of the screen
     * @param textureCoord texture coordinates to use. pass in null to use default.
     * @param x X-axis coordinates (-1 to 1)
     * @param y Y-axis coordinates (1 to -1)
     * @param width 'draw width' percentage of screen width (0 to 2)
     * @param height 'draw height' percentage of screen height (0 to 2)
     */
    public void draw(Vector2d textureCoord[], float x, float y, float width, float height) {
        float newX = x - width/2;
        float newY = y + height/2;
        
        if (textureCoord != null) {
            draw((float)textureCoord[0].x, (float)textureCoord[1].x, (float)textureCoord[2].x, (float)textureCoord[3].x, 
                 (float)textureCoord[0].y, (float)textureCoord[1].y, (float)textureCoord[2].y, (float)textureCoord[3].y,
                 newX, newX, newX + width, newX + width,
                 newY, newY - height, newY - height, newY);
        } else {
            draw(newX, newX, newX + width, newX + width,
                 newY, newY - height, newY - height, newY);
        }
    }

    /**
     * draw the texture using a transformation matrix 
     * @param matrix transformation matrix 
     */
    public void draw(Matrix3d matrix) {   
        glPushMatrix();
        
        Vector3d points[] = getTransformedQuad(matrix);
        Texture.this.draw(points);
        
        glPopMatrix();
    }
    
    /**
     * draw the texture using a transformation matrix 
     * @param textureCoord texture coordinates
     * @param matrix transformation matrix 
     */
    public void draw(Vector2d[] textureCoord, Matrix3d matrix) {   
        glPushMatrix();
        
        Vector3d points[] = getTransformedQuad(matrix);
        Texture.this.draw(textureCoord, points);
        
        glPopMatrix();
    }
    
    private Vector3d[] getTransformedQuad(Matrix3d matrix) {
        Vector3d tQuad[] = new Vector3d[4];
        
        //quad
        Vector3d point1 = new Vector3d(-0.5, 0.5, 1);
        Vector3d point2 = new Vector3d(-0.5, -0.5, 1);
        Vector3d point3 = new Vector3d(0.5, -0.5, 1);
        Vector3d point4 = new Vector3d(0.5, 0.5, 1);
                
        //tranform quad using the matrix
        point1.mul(matrix);
        point2.mul(matrix);
        point3.mul(matrix);
        point4.mul(matrix);
        
        tQuad[0] = point1;
        tQuad[1] = point2;
        tQuad[2] = point3;
        tQuad[3] = point4;
        
        return tQuad;
    }

    private void draw(Vector3d[] points) {
        Texture.this.draw((float)points[0].x, (float)points[1].x, (float)points[2].x, (float)points[3].x, 
             (float)points[0].y, (float)points[1].y, (float)points[2].y, (float)points[3].y);
    }
    
    private void draw(Vector2d[] textureCoord, Vector3d[] points) {
        Texture.this.draw((float)textureCoord[0].x, (float)textureCoord[1].x, (float)textureCoord[2].x, (float)textureCoord[3].x, 
             (float)textureCoord[0].y, (float)textureCoord[1].y, (float)textureCoord[2].y, (float)textureCoord[3].y,
             (float)points[0].x, (float)points[1].x, (float)points[2].x, (float)points[3].x, 
             (float)points[0].y, (float)points[1].y, (float)points[2].y, (float)points[3].y);
    }
    
    private void draw(float x1, float x2, float x3, float x4,
                      float y1, float y2, float y3, float y4) {        
        glBindTexture(GL_TEXTURE_2D, textureID);

        // Draw a textured quad
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0); glVertex3f(x1, y1, 0);
        glTexCoord2f(0, 1); glVertex3f(x2, y2, 0);
        glTexCoord2f(1, 1); glVertex3f(x3, y3, 0);
        glTexCoord2f(1, 0); glVertex3f(x4, y4, 0);
        glEnd();
    }
    
    private void draw(float tx1, float tx2, float tx3, float tx4,
                      float ty1, float ty2, float ty3, float ty4,
                      float x1, float x2, float x3, float x4,
                      float y1, float y2, float y3, float y4) {        
        glBindTexture(GL_TEXTURE_2D, textureID);

        // Draw a textured quad
        glBegin(GL_QUADS);
        glTexCoord2f(tx1, ty1); glVertex3f(x1, y1, 0);
        glTexCoord2f(tx2, ty2); glVertex3f(x2, y2, 0);
        glTexCoord2f(tx3, ty3); glVertex3f(x3, y3, 0);
        glTexCoord2f(tx4, ty4); glVertex3f(x4, y4, 0);
        glEnd();
    }
    
    /**
     * replace a sub-texture area with the given image
     * @param subImage given image
     * @param xOffset sub texture start point 
     * @param yOffset sub texture start point 
     * @param width sub texture width
     * @param height sub texture height
     * @return true if successful
     */
    public boolean setSubTexture(BufferedImage subImage, int xOffset, int yOffset, int width, int height) {
        if (xOffset < 0 || yOffset < 0) {
            System.err.println("Texture::setSubTexture: Given sub texture offset cannot be less than 0.");
        } else if ((xOffset + width) >= tWidth || (yOffset + height) >= tHeight) {
            System.err.println("Texture::setSubTexture: Given sub texture offset cannot be greater than texture size.");
        } else if (subImage == null) {
            System.err.println("Texture::setSubTexture: Given image cannot be null.");
        } else {
            ByteBuffer data;
            
            if (subImage.getWidth() != width || subImage.getHeight() != height) {
                BufferedImage resizedImage = resizeImage(subImage, width, height);
                data = convertToByteBuffer(resizedImage);
            } else {
                data = convertToByteBuffer(subImage);
            }
            
            return setSubTexture(data, xOffset, yOffset, width, height);
        }
        
        return false;
    }
    
    /**
     * replace a sub-texture area with the given image
     * @param subImage given image in ByteBuffer
     * @param xOffset sub texture start point 
     * @param yOffset sub texture start point 
     * @param width sub texture width
     * @param height sub texture height
     * @return true if successful
     */
    public boolean setSubTexture(ByteBuffer subImage, int xOffset, int yOffset, int width, int height) {
        if (xOffset < 0 || yOffset < 0) {
            System.err.println("Texture::setSubTexture: Given sub texture offset cannot be less than 0.");
        } else if ((xOffset + width) >= tWidth || (yOffset + height) >= tHeight) {
            System.err.println("Texture::setSubTexture: Given sub texture offset cannot be greater than texture size.");
        } else if (subImage == null) {
            System.err.println("Texture::setSubTexture: Given ByteBuffer cannot be null.");
        } else {
            glBindTexture(GL_TEXTURE_2D, textureID);
            glTexSubImage2D(GL_TEXTURE_2D, 0, xOffset, yOffset, width, height, GL_RGBA, GL_UNSIGNED_BYTE, subImage);
            return true;
        }
        
        return false;
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

    public static BufferedImage resizeImage(BufferedImage image, int width, int height) {
        BufferedImage newImage = new BufferedImage(width, height, image.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(image, 0, 0, width, height, null);
        return newImage;
    }
}
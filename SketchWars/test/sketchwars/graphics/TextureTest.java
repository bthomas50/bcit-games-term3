/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import static org.junit.Assert.*;
import org.junit.*;
import sketchwars.OpenGL;

/**
 *
 * @author A00807688
 */
public class TextureTest {
    private static final String testImage = "content/test.png";
    private static final int TEST_WIDTH = 64;
    private static final int TEST_HEIGHT = 64;
    private static final OpenGL opengl = new OpenGL();
            
    @BeforeClass
    public static void setupTest() throws IOException {
        opengl.init(false);
                
        BufferedImage image = new BufferedImage(TEST_WIDTH, TEST_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        File outputfile = new File(testImage);
        ImageIO.write(image, "png", outputfile);
    }
   
    @Test
    public void testTextureCreation() {
        Texture texture = Texture.loadTexture(testImage, false);
        
        int textureID = texture.getTextureID();
        
        assertNotEquals(-1, textureID);
        assertEquals(TEST_WIDTH, (int)texture.getTextureWidth());
        assertEquals(TEST_HEIGHT, (int)texture.getTextureHeight());
        
        assertEquals(1, texture.getTotalReferences());
        assertEquals(1, Texture.getTotalReferences(textureID));
        texture.dispose();
        assertEquals(texture.getTotalReferences(), 0);
        assertEquals(Texture.getTotalReferences(textureID), 0);
        
        Texture.disposeAllTextures();
    }
    
    @Test
    public void testTextureReferenceCounter() {
        Texture texture1 = Texture.loadTexture(testImage, false);
        Texture texture2 = Texture.loadTexture(testImage, false);
    
        assertEquals(2, texture1.getTotalReferences());
        assertEquals(2, Texture.getTotalReferences(texture2.getTextureID()));
        
        Texture texture3 = Texture.loadTexture(testImage, false);
        assertEquals(3, texture3.getTotalReferences());
        assertEquals(3, Texture.getTotalReferences(texture1.getTextureID()));
        
        texture2.dispose();
       // assertEquals(2, texture1.getTotalReferences());
        assertEquals(2, Texture.getTotalReferences(texture3.getTextureID()));
        
        texture1.dispose();
        assertEquals(1, texture3.getTotalReferences());
        assertEquals(1, Texture.getTotalReferences(texture3.getTextureID()));
        
        int textureID = texture3.getTextureID();
        texture3.dispose();
        assertEquals(0, Texture.getTotalReferences(textureID));
        
        Texture.disposeAllTextures();
    }
    
    @AfterClass
    public static void cleanup() throws IOException {
        File imageFile = new File(testImage);
        imageFile.delete();
        
        opengl.dispose();
    }
}

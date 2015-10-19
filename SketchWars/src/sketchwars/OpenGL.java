package sketchwars;

import java.nio.ByteBuffer;
import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import static org.lwjgl.system.MemoryUtil.NULL;
import sketchwars.input.KeyboardHandler;
/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class OpenGL {
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    
    // We need to strongly reference callback instances.
    private GLFWErrorCallback errorCallback;
    private KeyboardHandler   keyboardHandler;
 
    // The window handle
    private long window;
    
    public OpenGL() {
        this.keyboardHandler = new KeyboardHandler();
    }
    
    public void dispose() {
        try {
            // Release window and window callbacks
            glfwDestroyWindow(window);
            keyboardHandler.release();
            
            
        } finally { 
            // Terminate GLFW and release the GLFWerrorfun
            glfwTerminate();
            errorCallback.release();
        }
    }
    
    public void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));
 
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( glfwInit() != GL11.GL_TRUE )
            throw new IllegalStateException("Unable to initialize GLFW");
 
        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL11.GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL11.GL_FALSE); // the window will be resizable
  
        // Create the window
        window = glfwCreateWindow(WIDTH, HEIGHT, "Sketch Wars!", glfwGetPrimaryMonitor(), NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");
 
        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, keyboardHandler);
 
        // Get the resolution of the primary monitor
        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
            window,
            (GLFWvidmode.width(vidmode) - WIDTH) / 2,
            (GLFWvidmode.height(vidmode) - HEIGHT) / 2
        );
 
        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);
 
        // Make the window visible
        glfwShowWindow(window);
       
        GLContext.createFromCurrent();
        
        initCamera();
        
        
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the ContextCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities(true); // valid for latest build
        //GLContext.createFromCurrent(); // use this line instead with the 3.0.0a build
 
        // Set the clear color
        GL11.glClearColor(0.0f, 0.5f, 0.8f, 0.0f);
    }

    private void initCamera() {
        // enable alpha blending
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
         
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glViewport(0,0,WIDTH,HEIGHT);
        GL11.glOrtho(0.0, WIDTH, 0.0, HEIGHT, -1.0, 1.0);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
    
    public boolean windowsIsClosing() {
        return  glfwWindowShouldClose(window) != GL11.GL_FALSE;
    }
    
    public void beginUpdate() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // clear the framebuffer
    }
    
    public void endUpdate() {
        
        glfwSwapBuffers(window); // swap the color buffers 
        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents();
    }
}

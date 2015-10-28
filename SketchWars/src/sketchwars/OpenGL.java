package sketchwars;

import java.nio.ByteBuffer;
import org.joml.Vector2d;
import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import static org.lwjgl.system.MemoryUtil.NULL;
import sketchwars.input.*;
/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class OpenGL {    
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    // We need to strongly reference callback instances.
    private GLFWErrorCallback errorCallback;
    
    private final KeyboardHandler   keyboardHandler;
    private final GLFWMouseButtonCallback mouseButtonCallback;
    private final GLFWCursorPosCallback mousePosCallback;
    // The window handle
    private static long window;
    
    private static boolean fullscreen;
    
    public OpenGL() {
        keyboardHandler = new KeyboardHandler();
        mouseButtonCallback = new MouseHandler.ButtonCallback();
        mousePosCallback = new MouseHandler.PositionCallback();
    }
    
    public void dispose() {
        try {
            // Release window and window callbacks
            glfwDestroyWindow(window);
            keyboardHandler.release();
            mouseButtonCallback.release();
            mousePosCallback.release();
            
        } finally { 
            // Terminate GLFW and release the GLFWerrorfun
            glfwTerminate();
            errorCallback.release();
        }
    }
    
    public void init(boolean fullscreen) {
        OpenGL.fullscreen = fullscreen;
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
         
        // Get the resolution of the primary monitor
        long primaryMonitor = glfwGetPrimaryMonitor();
        ByteBuffer vidmode = glfwGetVideoMode(primaryMonitor);
         
        // Create the window
        if (fullscreen) {
            window = glfwCreateWindow(GLFWvidmode.width(vidmode), GLFWvidmode.height(vidmode), "Sketch Wars!", primaryMonitor, NULL);
        } else {
            window = glfwCreateWindow(WIDTH, HEIGHT, "Sketch Wars!", NULL, NULL);
        }
        
        if ( window == NULL )
                   throw new RuntimeException("Failed to create the GLFW window");
        
        if (!fullscreen) {
            // Center our window
            glfwSetWindowPos(
                window,
                (GLFWvidmode.width(vidmode) - WIDTH) / 2,
                (GLFWvidmode.height(vidmode) - HEIGHT) / 2
            );
        }
                
        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, keyboardHandler);
        glfwSetCursorPosCallback(window, mousePosCallback);
        glfwSetMouseButtonCallback(window, mouseButtonCallback);
            
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
        GL11.glOrtho(-1.0, 1.0, -1.0, 1.0, -1.0, 1.0);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        // GL11.glTranslated(-0.5, 0, 0);
        // GL11.glScaled(0.5, 0.5, 0.0);
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

    public static Vector2d getDisplaySize() {
        if (fullscreen) {
            // Get the resolution of the primary monitor
            long primaryMonitor = glfwGetPrimaryMonitor();
            ByteBuffer vidmode = glfwGetVideoMode(primaryMonitor);

            return new Vector2d(GLFWvidmode.width(vidmode), GLFWvidmode.height(vidmode));
        } else {
            return new Vector2d(WIDTH, HEIGHT);
        }
    }
    
    public static void hideMousePointer() {
        if (window != NULL) {
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN); 
        }
    }
    
    public static void showMousePointer() {
        if (window != NULL) {
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL); 
        }
    }
    
    public static float getAspectRatio() {
        Vector2d screen = getDisplaySize();
        return (float) (screen.x/screen.y);
    }
    
}

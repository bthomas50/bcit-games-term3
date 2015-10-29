package sketchwars.input;

import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.*;

public class KeyboardHandler extends GLFWKeyCallback {     
    public static int key;
    public static KeyState state;
    
    private static boolean[] keys = new boolean[65536];

    private static int curEvent = GLFW_RELEASE;
    private static int lastEvent = GLFW_RELEASE;
    
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
            // TODO Auto-generated method stub
            keys[key] = action != GLFW_RELEASE;
      
            if(action != GLFW_REPEAT) {
                
            }
            
            KeyboardHandler.key = key;
                curEvent = action;
    }

    public static void update() {
        if(curEvent == GLFW_RELEASE && lastEvent == GLFW_RELEASE) {
            state = KeyState.UP;
        } else if(curEvent == GLFW_RELEASE && lastEvent == GLFW_PRESS) {
            state = KeyState.RISING;
        } else if(curEvent == GLFW_PRESS && lastEvent == GLFW_RELEASE) {
            state = KeyState.FALLING;
        } else {
            state = KeyState.DOWN;
        }
        
        lastEvent = curEvent;
    }

    // boolean method that returns true if a given key
    // is pressed.
    public static boolean isKeyDown(int keycode) {
            return keys[keycode];
    }
}
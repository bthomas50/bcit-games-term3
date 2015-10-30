package sketchwars.input;

import java.util.ArrayList;
import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWCharCallback;

public class KeyboardHandler extends GLFWKeyCallback { 
    public static KeyState state;
    
    private static final boolean[] keys = new boolean[65536];

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        // TODO Auto-generated method stub
        keys[key] = action != GLFW_RELEASE;
    }


    // boolean method that returns true if a given key
    // is pressed.
    public static boolean isKeyDown(int keycode) {
        return keys[keycode];
    }
    
    public static void addCharListener(KeyCharListener listener) {
        CharCallback.addCharListener(listener);
    }
    
    public static class CharCallback extends GLFWCharCallback {
        private static final ArrayList<KeyCharListener> listeners = new ArrayList<>();
        @Override
        public void invoke(long window, int codepoint) {
            for (KeyCharListener li: listeners) {
                li.charTyped(codepoint);
            }
        }
        
        public static void addCharListener(KeyCharListener listener) {
            if (listener != null) {
                listeners.add(listener);
            }
        }
    }
}
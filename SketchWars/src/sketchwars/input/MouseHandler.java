package sketchwars.input;

import org.joml.Vector2d;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import static org.lwjgl.glfw.GLFW.*;
import sketchwars.OpenGL;

public class MouseHandler 
{
    public static int x;
    public static int y;
    public static float dWheelValue;
    public static float xNormalized;
    public static float yNormalized;
    
    public static KeyState state = KeyState.UP;
    public static DWheelState dwheelState = DWheelState.NONE;
    
    private static int curEvent = GLFW_RELEASE;
    private static int lastEvent = GLFW_RELEASE;
    

    public static void update()
    {
        if(curEvent == GLFW_RELEASE && lastEvent == GLFW_RELEASE)
        {
            state = KeyState.UP;
        }
        else if(curEvent == GLFW_RELEASE && lastEvent == GLFW_PRESS)
        {
            state = KeyState.RISING;
        }
        else if(curEvent == GLFW_PRESS && lastEvent == GLFW_RELEASE)
        {
            state = KeyState.FALLING;
        }
        else
        {
            state = KeyState.DOWN;
        }
        
        normalizeMousePosition();
        ScrollWheelCallback.update();
        
        lastEvent = curEvent;
    }

    

    public static class ButtonCallback extends GLFWMouseButtonCallback
    {
        @Override
        public void invoke(long window, int button, int action, int mods)
        {
            if(button == 0 && action != GLFW_REPEAT)
            {
                curEvent = action;
            }
        }
    }

    public static class ScrollWheelCallback extends GLFWScrollCallback
    {
        private static final int RESET_AFTER = 250;
        
        private static double dWheelY;
        private static long lastInvokeTime;
        private static double lastDWheelY;
    
        @Override
        public void invoke(long window, double xoffset, double yoffset) {
            
            ScrollWheelCallback.dWheelY = yoffset;
            MouseHandler.dWheelValue = (float)Math.abs(yoffset);
            ScrollWheelCallback.lastInvokeTime = System.currentTimeMillis();
                    
            if (yoffset > 0) {
                dwheelState = DWheelState.FORWARD;
            } else if (yoffset < 0) {
                dwheelState = DWheelState.BACKWARD;
            }
        }
        
        public static void update() {
            handleDWheelReset();
        }
        
        private static void handleDWheelReset() {
            long currentTime = System.currentTimeMillis();
            int timeSinceUpdate = (int) (currentTime - lastInvokeTime);
            
            if (ScrollWheelCallback.dWheelY == lastDWheelY &&
                    timeSinceUpdate > RESET_AFTER) {
                dwheelState = DWheelState.NONE;
            }

            lastDWheelY = ScrollWheelCallback.dWheelY;
        } 
    }
    
    public static class PositionCallback extends GLFWCursorPosCallback
    {
        @Override
        public void invoke(long window, double xPos, double yPos)
        {
            MouseHandler.x = (int) xPos;
            MouseHandler.y = (int) yPos;
        }
    }
    
    private static void normalizeMousePosition() {
        Vector2d screenSize = OpenGL.getDisplaySize();
        xNormalized = -1 + (float)((2/screenSize.x) * x);
        yNormalized = 1 - (float)((2/screenSize.y) * y);
    }
}
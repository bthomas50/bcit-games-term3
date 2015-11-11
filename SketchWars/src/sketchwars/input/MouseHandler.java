package sketchwars.input;

import org.joml.Vector2d;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import static org.lwjgl.glfw.GLFW.*;
import sketchwars.OpenGL;
import sketchwars.scenes.Camera;

public class MouseHandler 
{
    public static int x;
    public static int y;
    public static float dWheelValue;
    public static float xNormalized;
    public static float yNormalized;
    
    public static MouseState leftBtnState = MouseState.UP;
    public static MouseState rightBtnState = MouseState.UP;
    public static DWheelState dwheelState = DWheelState.NONE;
    
    private static int curEvent = GLFW_RELEASE;
    private static int lastEvent = GLFW_RELEASE;
    private static int mouseButton;
    
    private static Camera currentCamera;

    public static void update()
    {
        MouseState state;
        if(curEvent == GLFW_RELEASE && lastEvent == GLFW_RELEASE)
        {
            state = MouseState.UP;
        }
        else if(curEvent == GLFW_RELEASE && lastEvent == GLFW_PRESS)
        {
            state = MouseState.RISING;
        }
        else if(curEvent == GLFW_PRESS && lastEvent == GLFW_RELEASE)
        {
            state = MouseState.FALLING;
        }
        else
        {
            state = MouseState.DOWN;
        }
        
        if (mouseButton == 0) {
            leftBtnState = state;
        } else if (mouseButton == 1) {
            rightBtnState = state;
        }
        
        normalizeMousePosition();
        ScrollWheelCallback.update();
        
        lastEvent = curEvent;
    }

    public static void setCurrentCamera(Camera camera) {
        System.out.println("Set Camera: " + camera);
        MouseHandler.currentCamera = camera;
    }

    public static class ButtonCallback extends GLFWMouseButtonCallback
    {
        @Override
        public void invoke(long window, int button, int action, int mods)
        {
            if(action != GLFW_REPEAT)
            {
                curEvent = action;
            }
            
            MouseHandler.mouseButton = button;
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
        
        if (currentCamera == null) {
            xNormalized = -1 + (float)((2/screenSize.x) * x);
            yNormalized = 1 - (float)((2/screenSize.y) * y);
        } else {
            float cameraWidth = currentCamera.getWidth();
            float cameraHeight = currentCamera.getHeight();
            float cameraLeft = currentCamera.getLeft();
            float cameraTop = currentCamera.getTop();
            
            xNormalized = cameraLeft + (float)((cameraWidth/screenSize.x) * x);
            yNormalized = cameraTop - (float)((cameraHeight/screenSize.y) * y);
        }
    }
}
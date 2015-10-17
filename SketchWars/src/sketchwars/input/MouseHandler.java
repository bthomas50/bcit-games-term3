package sketchwars.input;

import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import static org.lwjgl.glfw.GLFW.*;

public class MouseHandler 
{
    public static int x;
    public static int y;
    public static MouseState state = MouseState.UP;

    private static int curEvent = GLFW_RELEASE;
    private static int lastEvent = GLFW_RELEASE;

    public static void update()
    {
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
        else//(curEvent == GLFW_RELEASE && lastEvent == GLFW_RELEASE)
        {
            state = MouseState.DOWN;
        }
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

    public static class PositionCallback extends GLFWCursorPosCallback
    {
        @Override
        public void invoke(long window, double xPos, double yPos)
        {
            MouseHandler.x = (int) xPos;
            MouseHandler.y = (int) yPos;
        }
    }
}
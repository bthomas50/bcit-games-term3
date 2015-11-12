package sketchwars.input;

import java.util.List;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import sketchwars.sound.SoundPlayer;

public class Input
{
    public static Input currentInput;

    public static void update() {
        MouseHandler.update();
    }
    
    public static void handleGameInput() {
        currentInput = new Input();

        if(KeyboardHandler.isKeyDown(GLFW_KEY_W)){
            currentInput.commands.add(new Command(CommandType.JUMP));
        }
        if(KeyboardHandler.isKeyDown(GLFW_KEY_S)){
            currentInput.commands.add(new Command(CommandType.CROUCH));
        }
        
        if(KeyboardHandler.isKeyDown(GLFW_KEY_A) && !KeyboardHandler.isKeyDown(GLFW_KEY_D)) {
            currentInput.commands.add(new Command(CommandType.MOVE_LEFT));
        } else if(KeyboardHandler.isKeyDown(GLFW_KEY_D) && !KeyboardHandler.isKeyDown(GLFW_KEY_A)) {
            currentInput.commands.add(new Command(CommandType.MOVE_RIGHT));
        } else {
            currentInput.commands.add(new Command(CommandType.STAND));
        }

        if(KeyboardHandler.isKeyDown(GLFW_KEY_SPACE)){
            currentInput.commands.add(new Command(CommandType.FIRE));
        }
        if(KeyboardHandler.isKeyDown(GLFW_KEY_ESCAPE)){
            currentInput.commands.add(new Command(CommandType.SHOWMENU));
        }
        //mimic behaviour of space firing.
        if(MouseHandler.leftBtnState == MouseState.FALLING || MouseHandler.leftBtnState == MouseState.DOWN) {
            currentInput.commands.add(
                new MouseCommand(
                    CommandType.MOUSE_FIRE, 
                    MouseHandler.xNormalized, 
                    MouseHandler.yNormalized));
        }
        
        //always aim with mouse.
        currentInput.commands.add(
            new MouseCommand(
                CommandType.MOUSE_AIM, 
                MouseHandler.xNormalized, 
                MouseHandler.yNormalized));

        if(KeyboardHandler.isKeyDown(GLFW_KEY_UP) && !KeyboardHandler.isKeyDown(GLFW_KEY_DOWN)) {
            currentInput.commands.add(new Command(CommandType.AIM_UP));
        } else if(KeyboardHandler.isKeyDown(GLFW_KEY_DOWN) && !KeyboardHandler.isKeyDown(GLFW_KEY_UP)) {
            currentInput.commands.add(new Command(CommandType.AIM_DOWN));
        }
        
        if(KeyboardHandler.isKeyDown(GLFW_KEY_1)){
            currentInput.commands.add(new Command(CommandType.SWITCH_1));
        }
        
        if(KeyboardHandler.isKeyDown(GLFW_KEY_2)){
            currentInput.commands.add(new Command(CommandType.SWITCH_2));
        }
        
        if(KeyboardHandler.isKeyDown(GLFW_KEY_3)){
            currentInput.commands.add(new Command(CommandType.SWITCH_3));
        }
        
        if(KeyboardHandler.isKeyDown(GLFW_KEY_4)){
            currentInput.commands.add(new Command(CommandType.SWITCH_4));
        }
        
        if(KeyboardHandler.isKeyDown(GLFW_KEY_5)){
            currentInput.commands.add(new Command(CommandType.SWITCH_5));
        }
        
        if(KeyboardHandler.isKeyDown(GLFW_KEY_6)){
            currentInput.commands.add(new Command(CommandType.SWITCH_6));
        }
        
        if(KeyboardHandler.isKeyDown(GLFW_KEY_7)){
            currentInput.commands.add(new Command(CommandType.SWITCH_7));
        }
        
        if(KeyboardHandler.isKeyDown(GLFW_KEY_8)){
            currentInput.commands.add(new Command(CommandType.SWITCH_8));
        }
        
        
    }

    private ArrayList<Command> commands;

    private Input() {
        commands = new ArrayList<>();
    }
    
    public Input(List<Command> commands) {
        this.commands = new ArrayList<>(commands);
    }

    public List<Command> getCommands() {
        return commands;
    }
}
package assn2;

import sketchwars.input.*;
import sketchwars.graphics.*;

import java.util.ArrayList;
import java.util.List;

public class UI implements GraphicsObject
{
    private List<Button> buttons;

    public UI()
    {
        buttons = new ArrayList<>();
    }

    public void addButton(Button b)
    {
        buttons.add(b);
    }

    public List<ButtonClickCommands> getCurrentCommands()
    {
        List<ButtonClickCommands> commands = new ArrayList<>();
        if(MouseHandler.state == MouseState.RISING)
        {
            for(Button b : buttons)
            {
                if(b.contains(MouseHandler.x, MouseHandler.y))
                {
                    commands.add(b.getCommand());
                }
            }
        }
        return commands;
    }

    @Override
    public void render()
    {
        for(Button b : buttons)
        {
            b.render();
        }
    }
}
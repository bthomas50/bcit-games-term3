package sketchwars.ui;

import sketchwars.input.*;
import sketchwars.graphics.*;

import java.util.ArrayList;
import java.util.List;

//a generic collection of buttons that generates commands of type T
public class UI<T> implements GraphicsObject
{
    private List<Button<T> > buttons;

    public UI()
    {
        buttons = new ArrayList<>();
    }

    public void addButton(Button<T> b)
    {
        buttons.add(b);
    }

    public List<T> getCurrentCommands()
    {
        List<T> commands = new ArrayList<>();
        if(MouseHandler.state == MouseState.RISING)
        {
            for(Button<T> b : buttons)
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
        for(Button<T> b : buttons)
        {
            b.render();
        }
    }

    @Override
    public boolean hasExpired()
    {
        return false;
    }
}
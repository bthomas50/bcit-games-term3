package assn2;

import assn2.ai.*;
import sketchwars.graphics.*;
import sketchwars.physics.BoundingBox;
import java.util.HashMap;

public class TamagotchiUI extends UI
{
    private HashMap<States, Texture> stateTextures;
    private FiniteStateMachine fsm;

    public TamagotchiUI(FiniteStateMachine fsm)
    {
        stateTextures = new HashMap<>();
        this.fsm = fsm;
    }

    public void putStateTexture(States s, Texture t)
    {
        stateTextures.put(s, t);
    }

    @Override
    public void render()
    {
        super.render();
        Texture tex = stateTextures.get(fsm.getCurrentState());
        if(tex != null)
        {
            tex.drawInScreenCoords(0, 0, 150, 75);
        }
    }

    public static UI create(FiniteStateMachine fsm)
    {
        TamagotchiUI ret = new TamagotchiUI(fsm);
        ret.addButton(new Button(new BoundingBox(450, 50, 550, 250), Texture.loadTexture("content/ui/start.png"), Texture.loadTexture("content/ui/start.png"), ButtonClickCommands.START));
        ret.addButton(new Button(new BoundingBox(450, 300, 550, 500), Texture.loadTexture("content/ui/feed.png"), Texture.loadTexture("content/ui/feed.png"), ButtonClickCommands.FEED));
        ret.addButton(new Button(new BoundingBox(450, 550, 550, 750), Texture.loadTexture("content/ui/play.png"), Texture.loadTexture("content/ui/play.png"), ButtonClickCommands.PLAY));
        ret.putStateTexture(States.SLEEPING, Texture.loadTexture("content/states/sleeping.png"));
        ret.putStateTexture(States.EATING, Texture.loadTexture("content/states/eating.png"));
        ret.putStateTexture(States.HUNGRY, Texture.loadTexture("content/states/hungry.png"));
        ret.putStateTexture(States.WAITING, Texture.loadTexture("content/states/waiting.png"));
        ret.putStateTexture(States.DEAD, Texture.loadTexture("content/states/dead.png"));
        ret.putStateTexture(States.PLAYING, Texture.loadTexture("content/states/playing.png"));
        return ret;
    }
}
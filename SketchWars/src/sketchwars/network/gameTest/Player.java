package sketchwars.network.gameTest;

import sketchwars.network.gameTest.InputHandler;

public class Player{

    private InputHandler input;
    private String username;

    public Player( InputHandler input, String username) {
        this.input = input;
        this.username = username;
    }

    public void tick() {
       
    }


    public String getUsername() {
        return this.username;
    }
}

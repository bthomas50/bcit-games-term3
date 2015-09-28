package sketchwars.network.gameTest;

import java.net.InetAddress;

import sketchwars.network.gameTest.InputHandler;

public class PlayerMP extends Player {

    public InetAddress ipAddress;
    public int port;

    public PlayerMP(InputHandler input, String username, InetAddress ipAddress, int port) {
        super(input, username);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public PlayerMP( String username, InetAddress ipAddress, int port) {
        super(null, username);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public void tick() {
        super.tick();
    }
}

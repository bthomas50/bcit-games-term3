package packets;

import java.io.Serializable;
import java.net.InetAddress;

public class PeerInfo implements Serializable {
    public final InetAddress ipAddress;
    public final int portNum;
    public final String username;
    public final int id;

    public PeerInfo(InetAddress ipAddress, int portNum, String username, int id) {
        this.ipAddress = ipAddress;
        this.portNum = portNum;
        this.username = username;
        this.id = id;
    }

    @Override
    public String toString() {
        return ipAddress + ":" + portNum + " " + username + " " + id;
    }
}
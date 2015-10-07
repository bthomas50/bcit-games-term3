package packets;

import java.io.Serializable;
import java.net.InetAddress;

public class PeerInfo implements Serializable {
    public InetAddress ipAddress;
    public int portNum;
    public String username;
    public int id;

    public PeerInfo(InetAddress ipAddress, int portNum, String username, int id) {
        this.ipAddress = ipAddress;
        this.portNum = portNum;
        this.username = username;
        this.id = id;
    }

    @Override
    public String toString() {
        return ipAddress + " " + portNum + " " + username + " " + id;
    }
}
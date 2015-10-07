package packets;


public class PacketStart extends Packet {

	public static final long serialVersionUID = 658099834261054358L;
	public PeerInfo[] peers;

	public PacketStart(PeerInfo[] peerInfo) {
		this.type = Type.StartGame;
        this.peers = peerInfo;
	}
	

    
}

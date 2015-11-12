package packets;


public class PacketStart extends Packet {

	public PeerInfo[] peers;
	public int randomSeed;

	public PacketStart(PeerInfo[] peerInfo, int randomSeed) {
		super(Type.StartGame);
        peers = peerInfo;
        this.randomSeed = randomSeed;
	}
}

package packets;

import network.GameSetting;


public class PacketStart extends Packet {

	public PeerInfo[] peers;
	public int randomSeed;
        public GameSetting setting;

	public PacketStart(PeerInfo[] peerInfo, int randomSeed, GameSetting setting) {
            super(Type.StartGame);
            peers = peerInfo;
            this.randomSeed = randomSeed;
            this.setting = setting;
	}
}

package packets;

public class PacketLoginBroadcast extends Packet {

	public static final long serialVersionUID = -5108707259646006986L;
	
	public PacketLoginBroadcast(int id, String username) {
		this.id = id;
		this.username = username;
		this.type = Type.LoginBroadcast;
	}
}

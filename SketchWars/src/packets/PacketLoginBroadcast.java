package packets;

public class PacketLoginBroadcast extends LobbyPacket {

	public static final long serialVersionUID = -5108707259646006986L;

	public PacketLoginBroadcast(int id, String username) {
		super(Type.LoginBroadcast, id, username);
	}
}

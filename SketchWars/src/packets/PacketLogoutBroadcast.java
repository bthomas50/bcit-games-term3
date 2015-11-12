package packets;

public class PacketLogoutBroadcast extends LobbyPacket {

	public static final long serialVersionUID = -3506726110624607899L;
	
	public PacketLogoutBroadcast(int id, String username) {
		super(Type.LogoutClient, id, username);
	}

}

package packets;

public class PacketLogoutBroadcast extends Packet {

	public static final long serialVersionUID = -3506726110624607899L;
	
	public PacketLogoutBroadcast(int id, String username) {
		super();
		
		this.id = id;
		this.username = username;
		this.type =  Type.LogoutClient;
	}

}

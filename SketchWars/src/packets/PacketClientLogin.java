package packets;

public class PacketClientLogin extends Packet {

	public static final long serialVersionUID = -739030353401952099L;
	
	public PacketClientLogin(String username) {
		super();
		
		this.username = username;
		this.type =  Type.LoginClient;
	}
	
}

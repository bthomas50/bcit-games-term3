package packets;

public class PacketClientLogin extends Packet {
	public static final long serialVersionUID = -739030353401952099L;
	public String username;

	public PacketClientLogin(String username) {
		super(Type.LoginClient);
		this.username = username;
	}
	
}

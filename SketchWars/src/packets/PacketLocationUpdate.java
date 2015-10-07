package packets;

public class PacketLocationUpdate extends Packet {
	
	public static final long serialVersionUID = -2476868517087276754L;
	
	public int x;
	public int y;
	
	public PacketLocationUpdate(int id, String username, int x, int y) {
		super();
		
		this.id = id;
		this.username = username;
		this.x = x;
		this.y = y;
		this.type = Type.LocationUpdate;
	}
}

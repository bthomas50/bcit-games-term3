package packets;

public class LobbyPacket extends Packet
{
	public int id;
	public String username;

	public LobbyPacket(Type t, int id, String name)
	{
		super(t);
		this.id = id;
		this.username = name;
	}
}
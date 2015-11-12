package packets;

import java.io.Serializable;

public class GamePacket extends Packet
{
	public byte frameId;
	public byte id;

	public GamePacket(Type type, byte id, byte frameId)
	{
		super(type);
		this.id = id;
		this.frameId = frameId;
	}
}
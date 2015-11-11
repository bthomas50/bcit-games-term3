package packets;

import java.io.Serializable;

public class GamePacket implements Serializable
{
	public Type type;
	public byte id;
	public byte frameId;

	public GamePacket(Type type, byte id, byte frameId)
	{
		this.type = type;
		this.id = id;
		this.frameId = frameId;
	}
}
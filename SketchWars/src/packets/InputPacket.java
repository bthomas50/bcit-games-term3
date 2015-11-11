package packets;

import sketchwars.input.Command;

import java.util.List;

public class InputPacket extends GamePacket
{
	public List<Command> commands;

	public InputPacket(byte id, byte frameId, List<Command> commands)
	{
		super(Type.Input, id, frameId);
		this.commands = commands;
	}
}
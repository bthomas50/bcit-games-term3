package packets;

import sketchwars.input.Command;

import java.util.ArrayList;
import java.util.List;

public class InputPacket extends GamePacket
{
	public ArrayList<Command> commands;

	public InputPacket(byte id, byte frameId, List<Command> commands)
	{
		super(Type.Input, id, frameId);
		this.commands = new ArrayList<>(commands);
	}
}
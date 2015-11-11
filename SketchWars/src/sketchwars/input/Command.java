package sketchwars.input;

import java.io.Serializable;

public class Command implements Serializable
{
	private CommandType t;

	public Command(CommandType type)
	{
		t = type;
	}

	public CommandType getType()
	{
		return t;
	}
}
package sketchwars.input;

public class MouseCommand extends Command
{
	private float x, y;

	public MouseCommand(CommandType t, float x, float y)
	{
		super(t);
		this.x = x;
		this.y = y;
	}

	public float getX()
	{
		return x;
	}

	public float getY()
	{
		return y;
	}
}
package packets;

import java.io.Serializable;

public class Packet implements Serializable {

	public static final long serialVersionUID = -3004381089229191414L;
	public int id;
	public String username;
	public Type type;
}

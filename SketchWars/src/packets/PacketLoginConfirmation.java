package packets;

public class PacketLoginConfirmation extends LobbyPacket {

	public static final long serialVersionUID = -7194499331875777756L;
	
	public PacketLoginConfirmation(int id) {
		super(Type.LoginConfirmation, id, "");
	}

}

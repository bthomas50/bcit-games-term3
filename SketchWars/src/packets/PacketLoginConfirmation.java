package packets;

public class PacketLoginConfirmation extends Packet {

	public static final long serialVersionUID = -7194499331875777756L;
	
	public PacketLoginConfirmation(int id) {
		this.id = id;
        this.type = Type.LoginConfirmation;
	}

}

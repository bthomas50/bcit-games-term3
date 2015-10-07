package entities;

import java.net.Socket;

public class ClientEntityForManagementOnServer extends BaseClientEntity {
	
	public Socket socket;
	
	public ClientEntityForManagementOnServer(int id, Socket socket) {
		super(id);
		this.socket = socket;
	}
	
}

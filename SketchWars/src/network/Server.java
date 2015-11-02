package network;

import entities.ClientEntityForManagementOnServer;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import packets.Packet;
import packets.PacketStart;
import packets.PeerInfo;

public class Server implements Runnable {
	public int port;
	public int currentClientId = 0;
	public ServerSocket socket = null;
	public boolean isRunning = false;
	public Thread runner = null;
	public HashMap<Integer, ClientEntityForManagementOnServer> clients = null;
	public InetAddress localAddress = null;

	public Server(int port) {
		this.port = port;
		clients = new HashMap<Integer, ClientEntityForManagementOnServer>();
		try {
			localAddress = getReachableLocalAddress();
			if(localAddress == null) {
				System.out.println("no suitable IP address found - check your network settings.");
			} else {
				System.out.println("server IP is " + localAddress);
			}
			socket = new ServerSocket(port, -1, localAddress);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private InetAddress getReachableLocalAddress() throws SocketException {
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		while(interfaces.hasMoreElements()) {
		    NetworkInterface inter = interfaces.nextElement();
		    Enumeration<InetAddress> addresses = inter.getInetAddresses();
		    while (addresses.hasMoreElements()) {
		        InetAddress addr = addresses.nextElement();
		        if(!addr.isLoopbackAddress() && !(addr instanceof Inet6Address)) {
		        	return addr;
		        }
		    }
		}
		return null;
	}

	@Override
	public void run() {
		synchronized (this) {
			runner = Thread.currentThread();
			isRunning = true;
		}

		while (isRunning()) {

			try {
				ClientEntityForManagementOnServer client = new ClientEntityForManagementOnServer(currentClientId,
						socket.accept());

				clients.put(currentClientId, client);

				new Thread(new ClientManager(this, client)).start();

				currentClientId++;  

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void startGame() {
		PeerInfo[] arrayOfPeers = new PeerInfo[clients.size()];
		int i = 0;
		for(ClientEntityForManagementOnServer client : clients.values()) {
			System.out.println(client.socket.getLocalAddress().getHostAddress());
			arrayOfPeers[i++] = new PeerInfo(client.socket.getInetAddress(), client.socket.getPort(), client.username, client.id);
		}
		PacketStart packet = new PacketStart(arrayOfPeers);
		Utils.broadcast(packet, clients.values());
	}

	public synchronized boolean isRunning() {
		return isRunning;
	}

	public synchronized void stop() {
		isRunning = false;

		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void broadcast(Packet packet) {
        Utils.broadcast(packet, clients.values());
	}

	public synchronized Collection<ClientEntityForManagementOnServer> getAllClients() {
		return clients.values();
	}

	public synchronized void removeClient(ClientEntityForManagementOnServer client) {
		clients.remove(client.id);
	}

	public synchronized int getClientCount() {
		return clients.size();
	}

}

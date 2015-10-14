package network;

import entities.*;
import main.InputHandler;
import packets.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Scanner;
import java.net.*;

public class Client {
	
	private int id;
	public InetAddress hostIP;
	public int port;
	private String username;
        private int clientId;
	public boolean isRunning = false;
	public Socket socket = null;
	public HashMap<Integer, ClientEntityForClients> clients = null;

	
	public Client(String host, int port, String username) throws IOException {
        this(InetAddress.getByName(host), port, username);
	}
    
    public Client(InetAddress addr, int port, String username) throws IOException {
        this.hostIP = addr;
        this.port = port;
        this.username = username;
                this.clientId = clientId;
        clients = new HashMap<Integer, ClientEntityForClients>();
        socket = new Socket(addr, port);
        isRunning = false;
    }

	public void run() {
        isRunning = true;
		// Receive thread. Just listens for incoming stuff
		new Thread(new ClientRunnable(this)).start();
		Utils.outgoing(new PacketClientLogin(getUsername()), socket);
	}

	public synchronized boolean isRunning() {
		return isRunning;
	}
	
	public synchronized void stop() {
		isRunning = false;
		
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    Socket getSocket() {
        return socket;
    }
	
    public void addClient(ClientEntityForClients client) {
        if(client.id != id) {
            clients.put(client.id, client);
        }
    }

    public void removeClient(int id) {
        clients.remove(id);
    }
}

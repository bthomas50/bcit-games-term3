package main;

import network.Client;
import network.Server;

import java.util.Scanner;
import java.io.IOException;

public class Main {
	
	public static void main(String[] args) {
		System.out.println("Do you want to be a server (1) or a client (2): ");
		
		Scanner in = new Scanner(System.in);
		int num = in.nextInt();
		
		if(num == 1)
		{
			System.out.println("Enter port: ");
			int port = in.nextInt();
			
			Server server = new Server(port);
			new Thread(server).start();
			
			// Server is running in a thread. you can do anything here. Since the server is also
			// a player, you can run a client here that also joins the server locally..just connect
			// to the port. Also, here you can also do all your graphics stuff etc.
			// Run the client here for this user as well.

			
			System.out.println("Enter username: ");
			String username = in.next();

			tryToRunClient("localhost", port, username);
		}
		else
		{
			System.out.println("Enter host: ");
			String host = in.next();
			
			System.out.println("Enter port: ");
			int port = in.nextInt();
			
			System.out.println("Enter username: ");
			String username = in.next();
			
			tryToRunClient(host, port, username);
		}

	}

	private static void tryToRunClient(String host, int port, String username) {
		try {
			Client client = new Client(host, port, username);
			client.run();
		} catch(IOException ex) {
			System.out.println(ex);
		}
	}
	
}

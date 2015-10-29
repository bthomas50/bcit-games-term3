package main;

import network.*;
import static sketchwars.util.Config.*;

import java.util.Scanner;
import java.io.IOException;
import java.net.InetAddress;

public class ServerMain
{
    public static void main(String[] args) 
    {
        appendToLibraryPath("lib/native/");
        Scanner in = new Scanner(System.in);

        System.out.println("Enter port: ");
        int port = in.nextInt();
        
        Server server = new Server(port);
        new Thread(server).start();
        new DiscoveryServer().start();
        System.out.println("Enter username: ");
        String username = in.next();

        tryToRunClient(server.localAddress, port, username);
    }

    private static void tryToRunClient(InetAddress addr, int port, String username) {
        try {
            Client client = new Client(addr, port, username);
            client.run();
        } catch(IOException ex) {
            System.out.println(ex);
        }
    }
}
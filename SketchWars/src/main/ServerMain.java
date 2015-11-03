package main;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;
import network.Client;
import network.DiscoveryServer;
import network.Server;
import static sketchwars.util.Config.appendToLibraryPath;

public class ServerMain
{
    private static String username="HOST";
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
    
    public static void startServer(int port, String username)
    {

        
    }
    
    public static void setHostUsername(String username) {
        ServerMain.username = username;
    }
    public static String getHostUsername() {
        return ServerMain.username;
    }
    

    public static void tryToRunClient(InetAddress addr, int port, String username) {
        try {
            Client client = new Client(addr, port, username);
            client.run();
        } catch(IOException ex) {
            System.out.println(ex);
        }
    }
    
    
}
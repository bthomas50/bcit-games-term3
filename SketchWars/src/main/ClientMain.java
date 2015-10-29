package main;

import network.*;
import static sketchwars.util.Config.*;

import java.util.Scanner;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;

public class ClientMain
{
    public static void main(String[] args) 
    {
        appendToLibraryPath("lib/native/");
        Scanner in = new Scanner(System.in);

        System.out.println("Enter port: ");
        int port = in.nextInt();
        
        System.out.println("Enter username: ");
        String username = in.next();

        DiscoveryClient client = new DiscoveryClient();
        client.start();
        while(!client.hasAvailableGames())
        {}
        client.signalStopListening();
        Collection<InetAddress> games = client.getAvailableGames();
        for(InetAddress addr : games)
        {
            tryToRunClient(addr, port, username);
            break;
        }
    }

    private static void tryToRunClient(InetAddress host, int port, String username) {
        try {
            Client client = new Client(host, port, username);
            client.run();
        } catch(IOException ex) {
            System.out.println(ex);
        }
    }
}
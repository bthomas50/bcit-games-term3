package main;

import network.*;

import java.util.Scanner;
import java.io.IOException;

public class ServerMain
{
    public static void main(String[] args) 
    {
        Scanner in = new Scanner(System.in);

        System.out.println("Enter port: ");
        int port = in.nextInt();
        
        Server server = new Server(port);
        new Thread(server).start();

        System.out.println("Enter username: ");
        String username = in.next();

        tryToRunClient("localhost", port, username);
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
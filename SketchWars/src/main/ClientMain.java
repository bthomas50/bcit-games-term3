package main;

import network.*;

import java.util.Scanner;
import java.io.IOException;

public class ClientMain
{
    public static void main(String[] args) 
    {
        Scanner in = new Scanner(System.in);

        System.out.println("Enter host: ");
        String host = in.next();
        
        System.out.println("Enter port: ");
        int port = in.nextInt();
        
        System.out.println("Enter username: ");
        String username = in.next();
        
        tryToRunClient(host, port, username);
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
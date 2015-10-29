package main;

import network.DiscoveryServer;
import java.io.IOException;

/**
 *
 * @author Salman
 */
public class LobbyServer {	
 

public static void main(String args[]) 
{
    DiscoveryServer serv = new DiscoveryServer();
    serv.start();

    // while(true)
    //     {
    //         // Create byte buffers to hold the messages to send and receive
    //         byte[] receiveData = new byte[1024];          

    //         // Create an empty DatagramPacket packet
    //         DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

    //         // Block until there is a packet to receive, then receive it  (into our empty packet)
    //         udpServerSocket.receive(receivePacket);           

    //         // Extract the message from the packet and make it into a string, then trim off any end characters
    //         String clientMessage = (new String(receivePacket.getData())).trim();
            
    //         // Get the IP address and the the port number which the received connection came from
    //         InetAddress clientIP = receivePacket.getAddress();    

    //         if(clientMessage.length()>1)
    //         {
    //             // Print some status messages
    //             System.out.println("Client Connected - Socket Address: " + receivePacket.getSocketAddress());
    //             System.out.println("Client message: \"" + clientMessage + "\"");          

    //             // Print out status message
    //             System.out.println("Client IP Address & Hostname: " + clientIP + ", " + clientIP.getHostName() + "\n");
    //         }      

    //         // Get the port number which the recieved connection came from
    //         int clientport = receivePacket.getPort();
    //         System.out.println("Adding "+clientport);
    //         portSet.add(clientport);

    //         // Response message			
    //         String returnMessage = clientMessage.toUpperCase();          
    //         System.out.println(returnMessage);
    //         // Create an empty buffer/array of bytes to send back 
    //         byte[] sendData  = new byte[1024];

    //         // Assign the message to the send buffer
    //         sendData = returnMessage.getBytes();

    //         for(Integer port : portSet) 
    //         {
    //                 System.out.println(port != clientport);
    //                 if(port != clientport) 
    //                 {
    //                         // Create a DatagramPacket to send, using the buffer, the clients IP address, and the clients port
    //                         DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientIP, port); 
    //                         System.out.println("Sending");
    //                         // Send the echoed message          
    //                         udpServerSocket.send(sendPacket);    
    //                 }
    //         }
    //     }
    }
}

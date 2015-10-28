/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashSet;

/**
 *
 * @author Salman
 */
public class LobbyServer {	
 
private static HashSet<Integer> portSet = new HashSet<Integer>();

public static void main(String args[]) throws Exception {

   // The default port     
int serverport = 7777;        
boolean broadCastMode = false;

    if (args.length < 1) 
    {
        System.out.println("Usage: UDPServer " + "Now using Port# = " + serverport);
    } 
    // Get the port number & host to use from the command line
    else 
    {            
        serverport = Integer.valueOf(args[0]).intValue();
        System.out.println("Usage: UDPServer " + "Now using Port# = " + serverport);
    }

    // Open a new datagram socket on the specified port, default host local
    DatagramSocket udpServerSocket = new DatagramSocket(serverport);        

    System.out.println("Server started...\n");
    
    
    while(broadCastMode)
    {
        udpServerSocket.setBroadcast(true);
        byte[] recvBuf = new byte[15000];
        DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
        udpServerSocket.receive(packet);
        
        //Packet received
        System.out.println(">>>Discovery packet received from: " + packet.getAddress().getHostAddress());
        System.out.println(">>>Packet received; data: " + new String(packet.getData()));
        
        //See if the packet holds the right command (message)
        String message = new String(packet.getData()).trim();
        if (message.equals("DISCOVER_FUIFSERVER_REQUEST")) 
        {
          byte[] sendData = "DISCOVER_FUIFSERVER_RESPONSE".getBytes();
          //Send a response
          DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
          udpServerSocket.send(sendPacket);
        }
        
    }

    while(true)
        {
            // Create byte buffers to hold the messages to send and receive
            byte[] receiveData = new byte[1024];          

            // Create an empty DatagramPacket packet
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            // Block until there is a packet to receive, then receive it  (into our empty packet)
            udpServerSocket.receive(receivePacket);           

            // Extract the message from the packet and make it into a string, then trim off any end characters
            String clientMessage = (new String(receivePacket.getData())).trim();
            
            // Get the IP address and the the port number which the received connection came from
            InetAddress clientIP = receivePacket.getAddress();    

            if(clientMessage.length()>1)
            {
                // Print some status messages
                System.out.println("Client Connected - Socket Address: " + receivePacket.getSocketAddress());
                System.out.println("Client message: \"" + clientMessage + "\"");          

                // Print out status message
                System.out.println("Client IP Address & Hostname: " + clientIP + ", " + clientIP.getHostName() + "\n");
            }      

            // Get the port number which the recieved connection came from
            int clientport = receivePacket.getPort();
            System.out.println("Adding "+clientport);
            portSet.add(clientport);

            // Response message			
            String returnMessage = clientMessage.toUpperCase();          
            System.out.println(returnMessage);
            // Create an empty buffer/array of bytes to send back 
            byte[] sendData  = new byte[1024];

            // Assign the message to the send buffer
            sendData = returnMessage.getBytes();

            for(Integer port : portSet) 
            {
                    System.out.println(port != clientport);
                    if(port != clientport) 
                    {
                            // Create a DatagramPacket to send, using the buffer, the clients IP address, and the clients port
                            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientIP, port); 
                            System.out.println("Sending");
                            // Send the echoed message          
                            udpServerSocket.send(sendPacket);    
                    }
            }
        }
    }
}

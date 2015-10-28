/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class LobbyClient {
    
    
 
    public static void main(String args[]) throws Exception {  
 
        //find host
        DatagramSocket udpClientSocket = new DatagramSocket();
        udpClientSocket.setBroadcast(true);
        
        //our special message
        byte[] sendData = "DISCOVER_FUIFSERVER_REQUEST".getBytes();
        
        //first try broadcast ip
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 8888);
        udpClientSocket.send(sendPacket);
        System.out.println( ">>> Request packet sent to: 255.255.255.255 (DEFAULT)");

        //next try other network interface
        // All all network interface to interface Enum
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) 
        {
                NetworkInterface networkInterface = interfaces.nextElement();

                if (networkInterface.isLoopback() || !networkInterface.isUp()) 
                {
                        continue; // Don't want to broadcast to the loopback interface
                }

                for (InterfaceAddress interfaceAddress: networkInterface.getInterfaceAddresses()) 
                {

                        InetAddress broadcast = interfaceAddress.getBroadcast();
                        if (broadcast == null) {
                                continue;
                        }

                        // Broadcast the message over each network interfaces in enum
                        try {
                                DatagramPacket sendPacket2 = new DatagramPacket(sendData, sendData.length, broadcast, 8888);
                                udpClientSocket.send(sendPacket2);

                        } catch (Exception e) {

                        }
                        System.out.println(">>> Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());
                }
        }
        //Now waiting for a response
        byte[] recvBuf = new byte[15000];
        DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
        udpClientSocket.receive(receivePacket);
        
        //We have a response
        System.out.println(">>> Broadcast response from server: " + receivePacket.getAddress().getHostAddress());
        
        
        //Check if the message is correct
        String message = new String(receivePacket.getData()).trim();
        if (message.equals("DISCOVER_FUIFSERVER_RESPONSE")) {
            //DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
            //setServerIp(receivePacket.getAddress());
            //Server IP
        }
        

        
        
        // The default port     
        int clientport = 7777;
        /*
        InetAddress host = localhost;
 
        if (args.length < 1) {
           System.out.println("Usage: UDPClient " + "Now using host = " + host + ", Port# = " + clientport);
        } 
        // Get the port number to use from the command line
        else {      
           //host = args[0];
           clientport = Integer.valueOf(args[0]).intValue();
           System.out.println("Usage: UDPClient " + "Now using host = " + host + ", Port# = " + clientport);
        }
        */
 
        // Get the IP address of the local machine - we will use this as the address to send the data to
        InetAddress ia = receivePacket.getAddress();
 
        SenderThread sender = new SenderThread(ia, clientport);
        sender.start();
        ReceiverThread receiver = new ReceiverThread(sender.getSocket());
        receiver.start();
    }
}      
 
class SenderThread extends Thread {
 
    private InetAddress serverIPAddress;
    private DatagramSocket udpClientSocket;
    private boolean stopped = false;
    private int serverport;
 
    public SenderThread(InetAddress address, int serverport) throws SocketException {
        this.serverIPAddress = address;
        this.serverport = serverport;
        // Create client DatagramSocket
        this.udpClientSocket = new DatagramSocket();
        this.udpClientSocket.connect(serverIPAddress, serverport);
    }
    public void halt() {
        this.stopped = true;
    }
    public DatagramSocket getSocket() {
        return this.udpClientSocket;
    }
 
    public void run() {       
        try {    
        	//send blank message
        	byte[] data = new byte[1024];
        	data = "".getBytes();
        	DatagramPacket blankPacket = new DatagramPacket(data,data.length , serverIPAddress, serverport);
            udpClientSocket.send(blankPacket);
            
        	// Create input stream
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            while (true) 
            {
                if (stopped)
                    return;
 
                // Message to send
                String clientMessage = inFromUser.readLine();
 
                if (clientMessage.equals("."))
                    break;
 
                // Create byte buffer to hold the message to send
                byte[] sendData = new byte[1024];
 
                // Put this message into our empty buffer/array of bytes
                sendData = clientMessage.getBytes();
 
                // Create a DatagramPacket with the data, IP address and port number
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverIPAddress, serverport);
 
                // Send the UDP packet to server
                System.out.println("I just sent: "+clientMessage);
                udpClientSocket.send(sendPacket);
 
                Thread.yield();
            }
        }
        catch (IOException ex) {
            System.err.println(ex);
        }
    }
}   
 
class ReceiverThread extends Thread {
 
    private DatagramSocket udpClientSocket;
    private boolean stopped = false;
 
    public ReceiverThread(DatagramSocket ds) throws SocketException {
        this.udpClientSocket = ds;
    }
 
    public void halt() {
        this.stopped = true;
    }
 
    public void run() {
 
        // Create a byte buffer/array for the receive Datagram packet
        byte[] receiveData = new byte[1024];
 
        while (true) {            
            if (stopped)
            return;
 
            // Set up a DatagramPacket to receive the data into
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            System.out.println("I am in the reader!");
            try {
                // Receive a packet from the server (blocks until the packets are received)
                udpClientSocket.receive(receivePacket);
                System.out.println("Am i receiving?");
                // Extract the reply from the DatagramPacket      
                String serverReply =  new String(receivePacket.getData(), 0, receivePacket.getLength());
 
                // print to the screen
                System.out.println("UDPClient: Response from Server: \"" + serverReply + "\"\n");
 
                Thread.yield();
            } 
            catch (IOException ex) {
            System.err.println(ex);
            }
        }
    }
}

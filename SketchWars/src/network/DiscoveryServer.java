package network;

import static network.NetworkConstants.*;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.IOException;

public class DiscoveryServer extends Thread
{
    private DatagramSocket sock;
    private boolean shouldContinue;
    byte[] recvBuf;
    public DiscoveryServer()
    {
        shouldContinue = true;
        recvBuf = new byte[15000];
    }

    @Override
    public void run()
    {
        try
        {
            createSocket();
            listenAndRespond();
        }
        catch(SocketException sex)
        {
            System.out.println("Error creating socket: " + sex.getMessage());
        }
        catch(IOException iex)
        {
            System.out.println("Error sending/receiving: " + iex.getMessage());
        }   
    }
    
    public synchronized void signalStopListening()
    {
        shouldContinue = false;
    }

    private synchronized boolean shouldContinue()
    {
        return shouldContinue;
    }

    private synchronized void sendReplyUnlessStopped(InetAddress addr, int port) throws IOException
    {
        if(shouldContinue)
        {
            byte[] sendData = DISCOVERY_RESPONSE.getBytes();
            //Send a response
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, addr, port);
            sock.send(sendPacket);
        }
    }

    private void createSocket() throws SocketException
    {
        System.out.println("Server started...");
        sock = new DatagramSocket(DISCOVERY_PORT);
        sock.setBroadcast(true);
    }

    private void listenAndRespond() throws IOException
    {
        while(shouldContinue())
        {
            DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
            sock.receive(packet);
            
            //Packet received
            System.out.println(">>>Packet received; data: " + new String(packet.getData()));
            
            //See if the packet holds the right command (message)
            String message = new String(packet.getData()).trim();
            if (message.equals(DISCOVERY_REQUEST))
            {
                sendReplyUnlessStopped(packet.getAddress(), packet.getPort());
            }
            
        }
    }
}
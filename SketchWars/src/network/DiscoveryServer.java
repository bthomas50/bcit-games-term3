package network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import static network.NetworkConstants.*;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import packets.*;

public class DiscoveryServer extends Thread
{
    private DatagramSocket sock;
    private boolean shouldContinue;
    byte[] recvBuf;
    public int gameServerPort;
    public DiscoveryServer(int gameServerPort)
    {
        shouldContinue = true;
        recvBuf = new byte[15000];
        this.gameServerPort = gameServerPort;
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
    
    public synchronized void halt()
    {
        shouldContinue = false;
        sock.close();
    }

    private synchronized boolean shouldContinue()
    {
        return shouldContinue;
    }

    private synchronized void sendReplyUnlessStopped(InetAddress addr, int port) throws IOException
    {
        if(shouldContinue)
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(new DiscoveryResponsePacket(gameServerPort));
            oos.flush();
            byte[] data = baos.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, addr, port);
            
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
            ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
            ObjectInputStream ois = new ObjectInputStream(bais);
            try
            {
                Packet pkt = (Packet) ois.readObject();
                //See if the packet holds the right command (message)
                if (pkt.type == Type.DiscoveryRequest)
                {
                    sendReplyUnlessStopped(packet.getAddress(), packet.getPort());
                }
            }
            catch(ClassNotFoundException ex)
            {
                System.out.println("Invalid packet type received");
            }
            
        }
        sock.close();
    }
}
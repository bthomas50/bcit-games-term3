package network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import static network.NetworkConstants.*;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import packets.DiscoveryRequestPacket;
import packets.DiscoveryResponsePacket;
import packets.Packet;
import packets.Type;

public class DiscoveryClient extends Thread
{
    private DatagramSocket sock;
    private boolean shouldContinue;
    private final byte[] recvBuf;
    private final List<GameListing> availableGames;

    public DiscoveryClient()
    {
        this.shouldContinue = true;
        recvBuf = new byte[15000];
        availableGames = new ArrayList<>();
    }

    @Override
    public void run()
    {
        try
        {
            createSocket();
            new SenderThread().start();
            listenForResponses();
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

    private void listenForResponses() throws IOException
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
                if (pkt.type == Type.DiscoveryResponse)
                {
                    foundGame(packet.getAddress(), ((DiscoveryResponsePacket)pkt).port);
                }
            }
            catch(ClassNotFoundException ex)
            {
                System.out.println("Invalid packet type received");
            }
        }
    }

    public synchronized boolean hasAvailableGames()
    {
        return !availableGames.isEmpty();
    }

    public synchronized List<GameListing> getAvailableGames()
    {
        List<GameListing> ret = new ArrayList<>();
        ret.addAll(availableGames);
        return ret;
    }

    private synchronized void foundGame(InetAddress addr, int port)
    {
        availableGames.add(new GameListing(addr, port, "testName"));
    }

    private void createSocket() throws SocketException
    {
        System.out.println("Server started...");
        sock = new DatagramSocket();
        sock.setBroadcast(true);
    }

    private class SenderThread extends Thread
    {
        private byte[] sendData;
        public SenderThread()
        {
            try
            {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(new DiscoveryRequestPacket());
                oos.flush();
                sendData = baos.toByteArray();
            }
            catch(IOException ioe)
            {}
        }

        @Override
        public void run()
        {
            while(shouldContinue())
            {
                try
                {
                    sendRequestOnAllInterfaces();
                    System.out.println("success");
                    Thread.sleep(DISCOVERY_POLLING_TIMEOUT);
                }
                catch(SocketException sex)
                {
                    System.out.println("Error creating socket: " + sex.getMessage());
                }
                catch(IOException iex)
                {
                    System.out.println("Error sending/receiving: " + iex.getMessage());
                }
                catch(InterruptedException e)
                {
                    System.out.println("Interrupted: " + e.getMessage());
                }
            }
        }

        private void sendRequestOnAllInterfaces() throws SocketException, IOException
        {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) 
            {
                NetworkInterface inter = interfaces.nextElement();
                if (inter.isLoopback() || !inter.isUp()) 
                {
                    continue; // Don't want to broadcast to the loopback interface
                }
                System.out.println(">>>Trying to send packet on interface: " + inter.getDisplayName());
                for (InterfaceAddress interfaceAddress: inter.getInterfaceAddresses())
                {
                    sendRequestToBroadcastAddress(interfaceAddress);
                }
            }
        }

        private void sendRequestToBroadcastAddress(InterfaceAddress addr) throws IOException
        {
            InetAddress broadcast = addr.getBroadcast();
            if (broadcast == null)
            {
                return;
            }
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, DISCOVERY_PORT);
            sock.send(sendPacket);
            System.out.println(">>> Request packet sent to: " + broadcast.getHostAddress());
        }
    }
    
    public class GameListing
    {
        public InetAddress addr;
        public int port;
        public String name;
        
        GameListing(InetAddress addr, int port, String name)
        {
            this.addr = addr;
            this.port = port;
            this.name = name;
        }
    }
}
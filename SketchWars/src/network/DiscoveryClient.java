package network;

import static network.NetworkConstants.*;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Collection;

public class DiscoveryClient extends Thread
{
    private DatagramSocket sock;
    private boolean shouldContinue;
    private byte[] recvBuf;
    private HashSet<InetAddress> availableGames;

    public DiscoveryClient()
    {
        this.shouldContinue = true;
        recvBuf = new byte[15000];
        availableGames = new HashSet<>();
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
            DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
            sock.receive(receivePacket);
            //We have a response
            System.out.println(">>> Broadcast response from server: " + receivePacket.getAddress().getHostAddress());
            //Check if the message is correct
            String message = new String(receivePacket.getData()).trim();
            if (message.equals(DISCOVERY_RESPONSE))
            {
                foundGame(receivePacket.getAddress());
            }
        }
    }

    public synchronized boolean hasAvailableGames()
    {
        return !availableGames.isEmpty();
    }

    public synchronized Collection<InetAddress> getAvailableGames()
    {
        return (Collection<InetAddress>) (availableGames.clone());
    }

    private synchronized void foundGame(InetAddress addr)
    {
        availableGames.add(addr);
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
            sendData = DISCOVERY_REQUEST.getBytes();
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
}
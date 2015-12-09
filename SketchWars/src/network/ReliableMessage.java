package network;

import packets.*;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

class ReliableMessage
{
    private static final int RESEND_DELAY_MILLIS = 5;

    private boolean wasAcknowledged;
    private final InputPacket packet;
    private final PeerInfo destination;

    ReliableMessage(InputPacket packet, PeerInfo dest)
    {
        wasAcknowledged = false;
        this.packet = packet;
        destination = dest;
    }

    void send(DatagramSocket sock)
    {
        new Thread(new Sender(sock)).start();
    }

    int getDestinationId() 
    {
        return destination.id;
    }

    byte getSequence() 
    {
        return packet.frameId;
    }

    synchronized void notifyAcknowledged() 
    {
        wasAcknowledged = true;
    }

    synchronized boolean wasAcknowledged() 
    {
        return wasAcknowledged;
    }

    private class Sender implements Runnable
    {
        private final DatagramSocket sock;

        private Sender(DatagramSocket s)
        {
            sock = s;
        }

        @Override
        public void run() 
        {
            try
            {
                byte[] data = serializeData();
                DatagramPacket packet = new DatagramPacket(data, data.length, destination.ipAddress, destination.portNum);
                sendUntilStopped(packet);
            }
            catch(IOException ioe)
            {
                System.out.println("Unable to serialize inputs: " + ioe.getMessage());
            }
        }

        private void sendUntilStopped(DatagramPacket packet) 
        {
            while(!wasAcknowledged()) 
            {
                try
                {
                    sock.send(packet);
                    Thread.sleep(RESEND_DELAY_MILLIS);
                }
                catch(IOException ioe)
                {
                    System.out.println("error sending message to " + destination.ipAddress + ":" + destination.portNum + " :: " + ioe.getMessage());
                    break;
                }
                catch(InterruptedException ie)
                {
                    System.out.println(ie.getMessage());
                    break;
                }
            }
        }

        private byte[] serializeData() throws IOException 
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(packet);
            oos.flush();
            return baos.toByteArray();
        }
    }
}
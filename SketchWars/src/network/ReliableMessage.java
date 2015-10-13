package network;

import packets.PeerInfo;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.io.IOException;
import java.lang.InterruptedException;

class ReliableMessage
{
    private static final int RESEND_DELAY_MILLIS = 5;

    private boolean wasAcknowledged;
    private byte[] data;
    private PeerInfo destination;

    ReliableMessage(byte[] data, PeerInfo dest)
    {
        wasAcknowledged = false;
        this.data = data;
        destination = dest;
    }

    void send(DatagramSocket sock)
    {
        new Thread(new Sender(sock)).start();
    }

    int getDestinationId() {
        return destination.id;
    }

    byte getSequence() {
        return data[1];
    }

    synchronized void notifyAcknowledged() {
        wasAcknowledged = true;
    }

    synchronized boolean wasAcknowledged() {
        return wasAcknowledged;
    }

    private class Sender implements Runnable
    {
        private DatagramSocket sock;

        private Sender(DatagramSocket s)
        {
            sock = s;
        }

        @Override
        public void run() 
        {
            DatagramPacket packet = new DatagramPacket(data, data.length, destination.ipAddress, destination.portNum);
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
    }
}
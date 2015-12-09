package network;

import packets.*;
import entities.*;

import java.io.IOException;
import java.util.Random;


class ClientRunnable implements Runnable {
    private final Client client;
    
    ClientRunnable(Client c) {
        client = c;
    }

    @Override
    public void run() {
        while(client.isRunning()) {
            Object obj = Utils.incoming(client.getSocket());
            if(obj == null)
            {
                break;
            }
            Type type = ((Packet) obj).type;
            
            if(type == Type.LoginBroadcast) {
                handleLogin((PacketLoginBroadcast) obj);                
            }
            else if(type == Type.LoginConfirmation) {
                // Set current client's ID
                PacketLoginConfirmation packet = (PacketLoginConfirmation) obj;
                System.out.println("my ID is " + packet.id);
                client.setId(packet.id);
            }
            else if(type == Type.StartGame) {
                handleStart((PacketStart) obj);
                return;
            }
            else if(type == Type.LogoutClient) {
                PacketLogoutBroadcast packet = (PacketLogoutBroadcast) obj;
                client.removeClient(packet.id);
            }
            else
            {
                // invalid packet
                System.err.println("received unknown packet");
            }
        }
    }

    public void handleStart(PacketStart packet) {
        System.out.println("starting game: all players:");
        for(PeerInfo peer : packet.peers) {
            System.out.println(peer);
        }
        try {
            Peer me = new Peer(client.getSocket().getLocalPort(), client.getId());
            for(PeerInfo peer : packet.peers) {
                me.addPeer(peer);
            }
            me.startListener();
            client.startGame(me, new Random(packet.randomSeed), packet.setting);
        } catch(IOException ioe) {
            System.out.println(ioe);
        }
    }

    private void handleLogin(PacketLoginBroadcast packet) {
        System.out.println(packet.username + "(id = " + packet.id + ") has connected!");
        client.addClient(new ClientEntityForClients(packet.id, packet.username));
    }
}
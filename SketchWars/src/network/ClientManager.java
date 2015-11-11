package network;

import entities.*;
import packets.*;

class ClientManager implements Runnable {

    public ClientEntityForManagementOnServer client;
    private Server server;

    public ClientManager(Server server, ClientEntityForManagementOnServer client) {
        this.client = client;
        this.server = server;
    }

    @Override
    public void run() {
        Object obj = Utils.incoming(client.socket);

        Type type = ((Packet) obj).type;

        if (type == Type.LoginClient) {
            handleClientLogin((PacketClientLogin) obj);
        } else if (type == Type.LocationUpdate) {
            handleLocationUpdate((PacketLocationUpdate) obj);
        } else if (type == Type.LogoutClient) {
            handleClientLogout((Packet) obj);
        } else if (type == Type.StartGame) {
            server.broadcast((Packet) obj);     
        } else {
            //ignore invalid packets
            System.err.println("received unknown packet.");
        }
    }

    private void handleClientLogin(PacketClientLogin packet) {
        // Register the username
        client.setUsername(packet.username);

        // Send confirmation to client
        Utils.outgoing(new PacketLoginConfirmation(client.id), client.socket);

        server.broadcast(new PacketLoginBroadcast(client.id, packet.username));
        System.out.println("Connected Clients: ");
        for(ClientEntityForManagementOnServer c : server.getAllClients()) {
            System.out.println(c.username);
        }
        if(server.getClientCount() > 1) {
            server.startGame();
        }
    }

    private void handleLocationUpdate(PacketLocationUpdate packet) {
        server.broadcast(packet);
        System.out.println("Server recienved location package");

        // Server can keep track of the current location of this
        // client.
        client.setX(packet.x);
        client.setY(packet.y);
    }

    private void handleClientLogout(Packet packet) {
        server.removeClient(client);
        System.out.println("total clients: " + server.getClientCount());
        server.broadcast(packet);
    }

}
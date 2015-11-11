package network;

import entities.*;
import packets.*;
import sketchwars.input.Input;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class Peer {
    private static final byte ACK = (byte)0xFF;
    private HashMap<Integer, PeerInfo> peers;

    private HashMap<Integer, Byte> lastInputsReceived;
    private HashMap<Integer, Byte> lastInputAcknowledged;
    private byte consumed;
    private HashMap<Integer, Input> inputs;
    private ArrayList<ReliableMessage> outgoingMessages;
    private DatagramSocket socket;

    private int localId;

    public Peer(int port, int localId) throws IOException {
        peers = new HashMap<>();
        socket = new DatagramSocket(port);
        lastInputsReceived = new HashMap<>();
        inputs = new HashMap<>();
        this.localId = localId;
        consumed = -1;
        outgoingMessages = new ArrayList<>();
    }

    public void broadcastInput(int frameNum) {
        System.out.println("sending inputs for frame: " + frameNum);
        InputPacket pkt = new InputPacket((byte)localId, (byte)frameNum, Input.currentInput.getCommands());
        for(PeerInfo peer : peers.values()) {
            sendReliably(pkt, peer);
        }
    }

    public void sendReliably(InputPacket data, PeerInfo dest) {
        ReliableMessage msg = new ReliableMessage(data, dest);
        synchronized(outgoingMessages) {
            outgoingMessages.add(msg);
        }
        msg.send(socket);
    }

    public void sendAck(int id, byte seq) throws IOException {
        System.out.println("sending ack to id " + id + " for seq: " + seq);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(new GamePacket(Type.Acknowledgement, (byte) localId, seq));
        oos.flush();
        byte[] data = baos.toByteArray();
        PeerInfo sendTo = peers.get(id);
        DatagramPacket packet = new DatagramPacket(data, data.length, sendTo.ipAddress, sendTo.portNum);
        try {
            socket.send(packet);
        } catch(IOException ioe) {
            System.err.println(ioe);
        }
    }

    //blocks until we get inputs from each peer.
    public Map<Integer, Input> getInputs(int frameNum) {
        byte seq = (byte) frameNum;
        while(true) {
            synchronized(lastInputsReceived) {
                if(hasAllInputs(seq)) {
                    consumed = seq;
                    HashMap<Integer, Input> ret = new HashMap<>();
                    ret.putAll(inputs);
                    System.out.println("got inputs for frame: " + frameNum);
                    return ret;
                }
            }
        }
        //unreachable, no return needed
    }

    private boolean hasAllInputs(byte seq) {
        for(byte b : lastInputsReceived.values()) {
            if(b != seq) {
                return false;
            }
        }
        return true;
    }

    public void startListener() {
        new Thread(new Listener(socket)).start();
    }

    public void addPeer(PeerInfo info) {
        peers.put(info.id, info);
        lastInputsReceived.put(info.id, (byte)-1);
    }

    private class Listener implements Runnable {
        private byte[] data = new byte[1024];
        private DatagramSocket socket;

        private Listener(DatagramSocket sock) {
            socket = sock;
        }

        @Override
        public void run() {
            while(true) {
                DatagramPacket packet = new DatagramPacket(data, data.length);
                try {
                    socket.receive(packet);
                    GamePacket pkt = deserializePacket(packet.getData());
                    switch(pkt.type)
                    {
                    case Acknowledgement:
                        receiveAck(pkt);
                        break;
                    case Input:
                        receiveInput((InputPacket)pkt);
                        break;
                    }
                } catch(IOException ioe) {
                    System.err.println("IOException: " + ioe.getMessage());
                } catch(ClassNotFoundException cnfe) {
                    System.err.println("ClassNotFoundException: " + cnfe.getMessage());
                }
            }
        }

        private GamePacket deserializePacket(byte[] pktData) throws IOException, ClassNotFoundException {
            ByteArrayInputStream bais = new ByteArrayInputStream(pktData);
            ObjectInputStream ois = new ObjectInputStream(bais);

            return (GamePacket) ois.readObject();
        }

        private void receiveAck(GamePacket packet) {
            int senderId = packet.id;
            byte seq = packet.frameId;
            ReliableMessage matchingMsg = null;
            synchronized(outgoingMessages) {
                for (ReliableMessage msg : outgoingMessages) {
                    if(msg.getDestinationId() == senderId && 
                       msg.getSequence() == seq) {
                        matchingMsg = msg;
                        break;
                    }
                }
                if(matchingMsg == null) {
                    System.out.println("unexpected ack received from " + senderId + " for frame " + seq);
                } else {
                    matchingMsg.notifyAcknowledged();
                    outgoingMessages.remove(matchingMsg);
                    System.out.println("processed ack from " + senderId + " for frame " + seq);
                }
            }
        }

        private void receiveInput(InputPacket packet) throws IOException {
            int senderId = packet.id;
            byte seq = packet.frameId;
            synchronized(lastInputsReceived) {
                byte desiredSeq = (byte)((int)consumed + 1);
                System.out.println("id = " + senderId + " says " + seq + ", wanted " + desiredSeq);
                if(seq == desiredSeq) {
                    lastInputsReceived.put(senderId, seq);
                    inputs.put(senderId, new Input(packet.commands));
                    sendAck(senderId, seq);
                }
            }
        }
    }
}
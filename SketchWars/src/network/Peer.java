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
        byte[] inputData = Input.currentInput.serializeByteArray();
        byte[] data = new byte[inputData.length + 2];
        data[0] = (byte) localId;
        data[1] = (byte) frameNum;
        System.arraycopy(inputData, 0, data, 2, inputData.length);
        for(PeerInfo peer : peers.values()) {
            sendReliably(data, peer);
        }
    }

    public void sendReliably(byte[] data, PeerInfo dest) {
        ReliableMessage msg = new ReliableMessage(data, dest);
        synchronized(outgoingMessages) {
            outgoingMessages.add(msg);
        }
        msg.send(socket);
    }

    public void sendAck(int id, byte seq) {
        System.out.println("sending ack to id " + id + " for seq: " + seq);
        byte[] data = new byte[3];
        data[0] = (byte) localId;
        data[1] = seq;
        data[2] = ACK;
        PeerInfo sendTo = peers.get(id);
        DatagramPacket packet = new DatagramPacket(data, 3, sendTo.ipAddress, sendTo.portNum);
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
        private byte[] data = new byte[256];
        private DatagramSocket socket;

        private Listener(DatagramSocket sock) {
            socket = sock;
        }

        @Override
        public void run() {
            while(true) {
                DatagramPacket packet = new DatagramPacket(data, 256);
                try {
                    socket.receive(packet);
                    byte[] pktData = packet.getData();
                    int senderId = pktData[0];
                    byte seq = pktData[1];
                    byte flag = pktData[2];
                    if(flag == ACK) {
                        receiveAck(senderId, seq);
                    } else {
                        receiveInput(senderId, seq, packet);
                    }
                    
                } catch(IOException ioe) {
                    System.err.println(ioe);
                }
            }
        }

        private void receiveAck(int senderId, byte seq) {
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

        private void receiveInput(int senderId, byte seq, DatagramPacket packet) {
            byte[] pktData = packet.getData();
            byte[] inputData = new byte[packet.getLength() - 2];
            System.arraycopy(pktData, 2, inputData, 0, inputData.length);
            Input input = Input.deserializeByteArray(inputData);
            synchronized(lastInputsReceived) {
                byte desiredSeq = (byte)((int)consumed + 1);
                System.out.println(packet.getAddress() + ":" + packet.getPort() + "(id = " + senderId + ") says " + seq + ", wanted " + desiredSeq);
                if(seq == desiredSeq) {
                    lastInputsReceived.put(senderId, seq);
                    inputs.put(senderId, input);
                    sendAck(senderId, seq);
                }
            }
        }
    }
}
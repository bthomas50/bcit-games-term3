package network;

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
    private final HashMap<Integer, PeerInfo> peers;

    private int windowStart, windowEnd;
    //for protecting the HashMaps.
    private final Object windowMutex;
    private final Object responseMutex;
    
    private final HashMap<Integer, Integer> windowCursors;
    
    private final HashMap<Integer, HashMap<Byte, Input>> inputs;
    private final ArrayList<ReliableMessage> outgoingMessages;
    private final DatagramSocket socket;

    private final int localId;

    public Peer(int port, int localId) throws IOException {
        windowMutex = new Object();
        responseMutex = new Object();
        peers = new HashMap<>();
        socket = new DatagramSocket(port);
        inputs = new HashMap<>();
        this.localId = localId;
        windowStart = 0;
        windowEnd = 4;
        windowCursors = new HashMap<>();
        outgoingMessages = new ArrayList<>();
    }

    public int getLocalId() {
        return localId;
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
        synchronized(responseMutex) {
            outgoingMessages.add(msg);
            msg.send(socket);
        }
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
        System.out.println(">>>>trying to get inputs for frame: " + frameNum);
        byte seq = (byte) frameNum;
        while(true) {
            boolean done;
            HashMap<Integer, Input> ret = new HashMap<>();
            synchronized(windowMutex) {
                done = hasAllInputs(frameNum);
                if(done) {
                    for(Integer i : inputs.keySet())
                    {
                        ret.put(i, inputs.get(i).get(seq));
                    }
                    System.out.println(">>>>got inputs for frame: " + frameNum);
                    windowStart = frameNum;
                    windowEnd = frameNum + 4;
                }
            }
            if(done)
            {
                return ret;
            }
        }
        //unreachable, no return needed
    }

    private boolean hasAllInputs(int frameNum) {
        for(int cursor : windowCursors.values()) {
            if(cursor < frameNum) {
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
        inputs.put(info.id, new HashMap<Byte, Input>());
        windowCursors.put(info.id, -1);
    }

    private class Listener implements Runnable {
        private final byte[] data = new byte[1024];
        private final DatagramSocket socket;

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
            synchronized(responseMutex) {
                for (ReliableMessage msg : outgoingMessages) {
                    if(msg.getDestinationId() == senderId && 
                       msg.getSequence() == seq) {
                        matchingMsg = msg;
                        break;
                    }
                }
                if(matchingMsg != null) {
                    matchingMsg.notifyAcknowledged();
                    outgoingMessages.remove(matchingMsg);
                }
            }
        }

        private void receiveInput(InputPacket packet) throws IOException {
            int senderId = packet.id;
            byte seq = packet.frameId;
            synchronized(windowMutex) {
                int diff = seq - (byte) windowStart;
                int frameNum = windowStart;
                if(diff < -128)
                {
                    frameNum += (255 + diff);
                }
                else
                {
                    frameNum += diff;
                }
                System.out.println("id = " + senderId + " says " + frameNum + ", wanted (" + windowStart + ", " + windowEnd + ")");
                windowCursors.put(senderId, frameNum);
                inputs.get(senderId).put(seq, new Input(packet.commands));
                sendAck(senderId, seq);
            }
        }
    }
}
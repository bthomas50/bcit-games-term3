package network;

import entities.*;
import packets.*;
import sketchwars.input.Input;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;

public class Peer {

    private HashMap<Integer, PeerInfo> peers;

    private HashMap<Integer, Boolean> haveInput;
    private HashMap<Integer, Input> inputs;

    private DatagramSocket socket;

    private int localId;

    public Peer(int port, int localId) throws IOException {
        peers = new HashMap<>();
        socket = new DatagramSocket(port);
        haveInput = new HashMap<>();
        inputs = new HashMap<>();
        this.localId = localId;
    }

    public void broadcastInput(int frameNum) {
        byte[] data = new byte[256];
        byte[] inputData = Input.currentInput.serializeByteArray();
        data[0] = (byte) localId;
        data[1] = (byte) frameNum;
        System.arraycopy(inputData, 0, data, 2, inputData.length);
        for(PeerInfo peer : peers.values()) {
            DatagramPacket packet = new DatagramPacket(data, inputData.length + 2, peer.ipAddress, peer.portNum);
            try {
                socket.send(packet);
            } catch(IOException ioe) {
                System.err.println(ioe);
            }
        }
    }

    //blocks until we get inputs from each peer.
    public Map<Integer, Input> getInputs() {
        while(true) {
            boolean haveAllInputs = true;
            synchronized(haveInput) {
                for(Boolean b : haveInput.values()) {
                    //System.out.println(b);
                    if(!b) {
                        haveAllInputs = false;
                        break;
                    }
                }
            }
            if(haveAllInputs) {
                break;
            }
        }
        synchronized(haveInput) {
            for(PeerInfo peer : peers.values()) {
                haveInput.put(peer.id, false);
            }
            HashMap<Integer, Input> ret = new HashMap<>();
            ret.putAll(inputs);
            return ret;
        }
    }

    public void startListener() {
        new Thread(new Listener(socket)).start();
    }

    public void addPeer(PeerInfo info) {
        peers.put(info.id, info);
        haveInput.put(info.id, false);
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
                    int frameNum = pktData[1];
                    byte[] inputData = new byte[packet.getLength() - 2];
                    System.arraycopy(pktData, 2, inputData, 0, inputData.length);
                    Input input = Input.deserializeByteArray(inputData);
                    System.out.println(packet.getAddress() + ":" + packet.getPort() + "(id = " + senderId + ") says " + frameNum);
                    synchronized(haveInput) {
                        haveInput.put(senderId, true);
                        inputs.put(senderId, input);
                    }
                } catch(IOException ioe) {
                    System.err.println(ioe);
                }
            }
        }
    }
}
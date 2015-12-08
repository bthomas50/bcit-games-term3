/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package packets;

/**
 *
 * @author a00861166
 */
public class DiscoveryResponsePacket extends Packet {
    public int port;
    public DiscoveryResponsePacket(int port) {
        super(Type.DiscoveryResponse);
        this.port = port;
    }
}

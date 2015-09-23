
package sketchwars.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import sketchwars.graphics.GraphicsObject;


/**
 *
 * @author Salman Shaharyar
 */
public class GameClient extends Thread{
    
    private InetAddress ipAddress;
    private DatagramSocket socket;
    private GraphicsObject game; 
    
    public GameClient(GraphicsObject game, String ipAddress)
    {
        this.game = game;
        try
        {
            this.socket = new DatagramSocket();
            this.ipAddress = InetAddress.getByName(ipAddress);
        } catch (SocketException e){
            e.printStackTrace();
        }
         catch (UnknownHostException e){
            e.printStackTrace();
        }
    }
    
    public void run()
    {
        while(true)
        {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try
            {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Server > " + new String(packet.getData()));
        }
    }
    
    public void sendData(byte[] data){
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress,1331);
        try
        {
            socket.send(packet);
        }catch(IOException e)
        {
            e.printStackTrace();
        }   
         
        
    }
}

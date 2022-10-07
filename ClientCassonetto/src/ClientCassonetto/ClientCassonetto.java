package ClientCassonetto;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.ArrayList;

/**
 *
 * @author JAKUBBOCIAN
 */
public class ClientCassonetto {
    private DatagramSocket socket;
    private String IP_address;
    private int UDP_port;

    public ClientCassonetto(String host, int port) throws SocketException {
        socket = new DatagramSocket();
        socket.setSoTimeout(1000);
        IP_address = host;
        UDP_port = port;
    }
    
    public void closeSocket(){
        socket.close();
    }

    public int registra() throws UnknownHostException, IOException{
        DatagramPacket datagram;
        ByteBuffer in, out;
        InetAddress address;
        byte[] buffer;
        
        out = ByteBuffer.allocate(1);
        out.put((byte)1);
        address = InetAddress.getByName(IP_address);
        datagram = new DatagramPacket(out.array(), 1, address, UDP_port);
        socket.send(datagram);
        
        buffer = new byte[4];
        datagram = new DatagramPacket(buffer, buffer.length);
        socket.receive(datagram);
        if ( datagram.getAddress().equals(address) && datagram.getPort() == UDP_port) {
            in = ByteBuffer.wrap(datagram.getData());
            return in.getInt();
        }
        else {
            throw new SocketTimeoutException();
        }
    }
    
    public static void main(String[] args) {
        ArrayList cassonetti = new ArrayList();
        ClientCassonetto client;
        try {
            client = new ClientCassonetto("127.0.0.1", 12345);
            cassonetti.add(new Cassonetto(new Tessera(client.registra())));
            System.out.println(((Cassonetto)(cassonetti.get(0))).getIdTessera());
            client.closeSocket();
        }
        catch(SocketException exception) {
            System.err.println("Errore creazione socket!");
        }
        catch (IOException exception) {
            System.err.println("Errore di comunicazione!");
        }
    }

}

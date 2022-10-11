package ClientCassonetto;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public void closeSocket() {
        socket.close();
    }

    public int registra() throws UnknownHostException, IOException {
        DatagramPacket datagram;
        ByteBuffer in, out;
        InetAddress address;
        byte[] buffer;

        out = ByteBuffer.allocate(1);
        out.put((byte) 1);
        address = InetAddress.getByName(IP_address);
        datagram = new DatagramPacket(out.array(), 1, address, UDP_port);
        socket.send(datagram);

        buffer = new byte[4];
        datagram = new DatagramPacket(buffer, buffer.length);
        socket.receive(datagram);
        if (datagram.getAddress().equals(address) && datagram.getPort() == UDP_port) {
            in = ByteBuffer.wrap(datagram.getData());
            return in.getInt();
        } else {
            throw new SocketTimeoutException();
        }
    }

    public boolean apri(Tessera t) throws UnknownHostException, IOException {
        DatagramPacket datagram;
        ByteBuffer in, out;
        InetAddress address;
        byte[] buffer;

        out = ByteBuffer.allocate(1);
        out.put((byte) 3);
        address = InetAddress.getByName(IP_address);
        datagram = new DatagramPacket(out.array(), 1, address, UDP_port);
        socket.send(datagram);

        buffer = new byte[1];
        datagram = new DatagramPacket(buffer, buffer.length);
        do {
            socket.receive(datagram);
            in = ByteBuffer.wrap(datagram.getData());
        } while (in.get()!=1);
        
        out = ByteBuffer.allocate(4);
        out.putInt(t.getId());
        address = InetAddress.getByName(IP_address);
        datagram = new DatagramPacket(out.array(), 4, address, UDP_port);
        socket.send(datagram);
        
        int ris;
        
        buffer = new byte[1];
        datagram = new DatagramPacket(buffer, buffer.length);
        socket.receive(datagram);
        in = ByteBuffer.wrap(datagram.getData());
        ris = in.get();
        System.out.println("ricevuto: " + ris);
        return ris==1;
    }
    
    public boolean disattiva(Tessera t) throws IOException{
        DatagramPacket datagram;
        ByteBuffer in, out;
        InetAddress address;
        byte[] buffer;

        out = ByteBuffer.allocate(1);
        out.put((byte) 2);
        address = InetAddress.getByName(IP_address);
        datagram = new DatagramPacket(out.array(), 1, address, UDP_port);
        socket.send(datagram);

        buffer = new byte[1];
        datagram = new DatagramPacket(buffer, buffer.length);
        do {
            socket.receive(datagram);
            in = ByteBuffer.wrap(datagram.getData());
        } while (in.get()!=1);
        
        out = ByteBuffer.allocate(4);
        out.putInt(t.getId());
        address = InetAddress.getByName(IP_address);
        datagram = new DatagramPacket(out.array(), 4, address, UDP_port);
        socket.send(datagram);
        
        buffer = new byte[1];
        datagram = new DatagramPacket(buffer, buffer.length);
        socket.receive(datagram);
        in = ByteBuffer.wrap(datagram.getData());
        if(in.get()==1)
            return true;
        return false;
    }

    public static void main(String[] args) {
        Tessera t = null;
        ClientCassonetto client;
        int scelta = 0;
        Scanner input = new Scanner(System.in); 
        
        try {
            client = new ClientCassonetto("127.0.0.1", 12345);
            do {
                System.out.println("--MENU--");
                System.out.println("1. Crea tessera");
                System.out.println("2. Disattiva tessera");
                System.out.println("3. Apri cassonetto");
                System.out.println("4. Uscita");
                scelta = input.nextInt();
                try {
                    switch (scelta) {
                        case 1:
                            t = new Tessera(client.registra());
                            System.out.println("Tessera creata, ID tessera: " + t.getId());
                            break;
                        case 2:
                            if (t == null) {
                                System.out.println("Nessuna tessera rilevata!");
                                break;
                            }
                            if(client.disattiva(t))
                                System.out.println("Tessera disattivita");
                            else
                                System.out.println("Non è stato possibile disattivare la tessera");
                            break;
                        case 3:
                            if (t == null) {
                                System.out.println("Nessuna tessera rilevata!");
                                break;
                            }
                            if(client.apri(t))
                                System.out.println("Cassonetto aperto");
                            else
                                System.out.println("Non è stato possibile aprire il cassonetto");
                            break;
                        case 4:
                            System.out.println("Uscita...");
                            client.closeSocket();
                            break;
                    }
                } catch (SocketException exception) {
                    System.err.println("Errore creazione socket!");
                } catch (IOException exception) {
                    System.err.println("Errore di comunicazione!");
                }
            } while (scelta != 4);
        } catch (SocketException ex) {
            System.err.println("Errore creazione socket!");
        }

    }

}

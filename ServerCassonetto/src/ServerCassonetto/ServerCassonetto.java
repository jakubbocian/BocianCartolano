/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ServerCassonetto;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JAKUBBOCIAN
 */
public class ServerCassonetto {

    /**
     * @param args the command line arguments
     */
    ArrayList<Tessera> tessereReg = new ArrayList();
    DatagramSocket socket;

    public ServerCassonetto(int port) throws SocketException {

        DatagramSocket socket = new DatagramSocket(port);
        socket.setSoTimeout(2000);

    }

    public static void main(String[] args) {
        // TODO code application logic here
        DatagramPacket answer, request;
        byte[] buffer = new byte[1];
        ByteBuffer data;
        int reqtype;

        try {
            ServerCassonetto server = new ServerCassonetto(12345);

            while (true) {

                request = new DatagramPacket(buffer, buffer.length);

                server.socket.receive(request);
                data = ByteBuffer.wrap(buffer, 0, 4);
                
                reqtype = data.getInt();
                System.out.println("Richiesta n: " + reqtype);
                
                
                /*answer = new DatagramPacket( data.array(), 8, request.getAddress(),
                request.getPort()); server.socket.send(answer);
                server.socket.send(answer);*/
            }

        } catch (SocketException ex) {
            System.err.println("Errore!");
        } catch (IOException ex) {

        }

    }

    public int creaTessera() {

        Tessera t = new Tessera(this.tessereReg.size() + 1);
        tessereReg.add(t);
        return t.getId();

    }

    public boolean checkTessera(int id) {

        for (Tessera t : tessereReg) {

            if (t.getId() == id) {
                return true;
            }

        }

        return false;
    }

    public void eliminaTessera(int id) {

        tessereReg.remove(id);

    }

}

package ServerCassonetto;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.ArrayList;

public class ServerCassonetto extends Thread{
    
    private ArrayList <Tessera> tessere = new ArrayList();

    private DatagramSocket socket;
    
    // costruttore (richiede il numero di porta del servizio)
    public ServerCassonetto(int port) throws SocketException {
        socket = new DatagramSocket(port);
        socket.setSoTimeout(5000); // 1000ms = 1s
    }

    public void run() {
        byte[] buffer = new byte[1];
        ByteBuffer data;
        DatagramPacket answer, request;
        
        int idTessera;
        System.out.println("Attendo richiesta....");
        
        while (!Thread.interrupted()) {
            try {
                request = new DatagramPacket(buffer, buffer.length);
                // attesa ricezione datagram di richiesta (tempo massimo di attesa: 1s)
                socket.receive(request);
                // incapsulazione del buffer della richiesta in un byte-buffer della dimensione di 4 valori float
                data = ByteBuffer.wrap(buffer, 0, 1);
                
                int richiesta = data.get();
               
                
                if(richiesta == 1){
                    
                    System.out.println("Richiesta 1");
                    data = ByteBuffer.wrap(buffer, 0, 4);
                    data.putInt(creaTessera());
                    answer = new DatagramPacket( data.array(), 4, request.getAddress(),
                    request.getPort()); 
                    socket.send(answer);
                    
                }
                
                /*
                // incapsulazione del buffer della risposta in un byte-buffer della dimensione di 1 valore double
                data = ByteBuffer.wrap(buffer, 0, 8);
                // inserimento del valore double nel byte-buffer
                // costruzione del datagram da trasmettere a partire dal contenuto del byte-buffer
                answer = new DatagramPacket( data.array(), 8, request.getAddress(),
                request.getPort()); socket.send(answer);
                socket.send(answer);*/
            }
            catch (SocketTimeoutException exception) {
            }
            catch (IOException exception) {
            }
        }
        socket.close(); // chiusura del socket
    }

    public static void main(String[] args) {
        int c;

        try {
            ServerCassonetto server = new ServerCassonetto(1234);
            server.start();
            c = System.in.read();
            server.interrupt();
            server.join();
        }
        catch (IOException exception) {
            System.err.println("IO!");
        }
        catch (InterruptedException exception) {
            System.err.println("IE!");
        }
    }
    
    public int creaTessera() {

        Tessera t = new Tessera(this.tessere.size() + 1);
        tessere.add(t);
        return t.getId();

    }

    public boolean checkTessera(int id) {

        for (Tessera t : tessere) {

            if (t.getId() == id) {
                return true;
            }
        }

        return false;
    }

    public void eliminaTessera(int id) {

        tessere.remove(id);

    }
}

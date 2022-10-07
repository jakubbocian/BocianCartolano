package ServerCassonetto;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerCassonetto extends Thread {

    private ArrayList<Tessera> tessere = new ArrayList();

    private DatagramSocket socket;

    // costruttore (richiede il numero di porta del servizio)
    public ServerCassonetto(int port) throws SocketException {
        socket = new DatagramSocket(port);
        socket.setSoTimeout(5000); // 1000ms = 1s
    }

    public void run() {
        byte[] buffer = new byte[1024];
        ByteBuffer data;
        DatagramPacket answer, request;

        int idTessera;
        System.out.println("Attendo richiesta....");

        while (!Thread.interrupted()) {
            try {
                request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);
                data = ByteBuffer.wrap(buffer, 0, 1);

                int richiesta = data.get();

                if (richiesta == 1) {

                    System.out.println("Richiesta 1...");
                    data = ByteBuffer.wrap(buffer, 0, 4);
                    data.putInt(creaTessera());
                    answer = new DatagramPacket(data.array(), 4, request.getAddress(),
                            request.getPort());
                    socket.send(answer);

                } else if (richiesta == 2) {

                    System.out.println("Richiesta 2...");

                    byte[] buffer_t = new byte[4];
                    ByteBuffer data_t;
                    DatagramPacket request_t;

                    boolean ricevuta = false;
                    int id_disattiva = -1;

                    while (!ricevuta) {

                        System.out.println("Attendo tessera da disattivare...");

                        request_t = new DatagramPacket(buffer_t, buffer_t.length);
                        socket.receive(request_t);

                        data_t = ByteBuffer.wrap(buffer_t, 0, 4);

                        id_disattiva = data_t.getInt();

                        for (Tessera t : tessere) {

                            if (t.getId() == id_disattiva) {
                                t.setValida(false);
                                ricevuta = true;
                            }

                        }

                    }

                } else if (richiesta == 3) {

                    System.out.println("Richiesta 3...");

                    byte[] buffer_t = new byte[1];
                    ByteBuffer data_t;
                    DatagramPacket request_t;

                    boolean ricevuta = false;
                    int id_controlla = -1;
                    int ris = 1;

                    while (!ricevuta) {

                        System.out.println("Attendo tessera da controllare...");

                        request_t = new DatagramPacket(buffer_t, buffer_t.length);
                        socket.receive(request_t);

                        data_t = ByteBuffer.wrap(buffer_t, 0, 4);

                        id_controlla = data_t.getInt();

                        for (Tessera t : tessere) {

                            if (t.getId() == id_controlla) {

                                if (!t.isValida() || !controlla_data(t.getU_apertura())) {
                                    ris = 0;
                                }

                                ricevuta = true;
                            }

                        }

                    }

                    data = ByteBuffer.wrap(buffer, 0, 4);
                    data.putInt(ris);
                    answer = new DatagramPacket(data.array(), 4, request.getAddress(), request.getPort());
                    socket.send(answer);

                }

            } catch (SocketTimeoutException exception) {
            } catch (IOException exception) {
            }
        }
        socket.close(); // chiusura del socket
    }

    public boolean controlla_data(Date d) {

        Date current_date = new Date();

        long time_difference = d.getTime() - current_date.getTime();
        /*int differenza_ore = ((int)time_difference / (1000*60*60)) % 24;  
        
        if(differenza_ore < 72)
            return false;
        
         */

        int differenza_secondi = ((int) time_difference / 1000) % 60;

        if (differenza_secondi < 7) {
            return false;
        }

        return true;

    }

    public void ack(InetAddress ip, int port) {

        byte[] buffer = new byte[1];
        ByteBuffer data;
        DatagramPacket answer;
        try {
            
            data = ByteBuffer.wrap(buffer, 0, 1);
            data.put((byte)1);
            answer = new DatagramPacket(data.array(), 1, ip, port);
            socket.send(answer);
            
        } catch (IOException ex) {
            Logger.getLogger(ServerCassonetto.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) {
        int c;

        try {
            ServerCassonetto server = new ServerCassonetto(12345);
            server.start();
            c = System.in.read();
            server.interrupt();
            server.join();
        } catch (IOException exception) {
            System.err.println("IO!");
        } catch (InterruptedException exception) {
            System.err.println("IE!");
        }
    }

    public int creaTessera() {

        Tessera t = new Tessera(this.tessere.size() + 1);
        tessere.add(t);
        return t.getId();

    }

    public void eliminaTessera(int id) {

        tessere.remove(id);

    }
}

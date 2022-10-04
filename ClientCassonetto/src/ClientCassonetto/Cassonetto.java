/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ClientCassonetto;

import java.net.DatagramSocket;
import java.util.Date;

/**
 *
 * @author JAKUBBOCIAN
 */
public class Cassonetto {
    private Date ultimaApertura;
    private Tessera tess;

    public Cassonetto(Tessera tess) {
        this.ultimaApertura = null;
        this.tess = tess;
    }
    
    public int getIdTessera(){
        return tess.getId();
    }
    
}

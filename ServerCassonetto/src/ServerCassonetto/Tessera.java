/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerCassonetto;

import java.util.Date;

/**
 *
 * @author JAKUBBOCIAN
 */
public class Tessera {
    private int id;
    private boolean valida;
    private Date u_apertura;    

    public Tessera(int id) {
        this.id = id;
        this.valida = true;
        this.u_apertura = new Date(2000, 1, 1);
    }

    public boolean isValida() {
        return valida;
    }

    public void setValida(boolean valida) {
        this.valida = valida;
    }
    
    public void setU_apertura(){
    
        this.u_apertura = new Date(); //data corrente
    }

    public int getId() {
        return id;
    }
    
    public Date getU_apertura(){
        return this.u_apertura;
    }
}

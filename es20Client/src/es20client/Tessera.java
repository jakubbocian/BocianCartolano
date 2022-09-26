/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es20client;

/**
 *
 * @author JAKUBBOCIAN
 */
public class Tessera {
    private int id;
    private boolean valida;

    public Tessera(int id) {
        this.id = id;
        this.valida = true;
    }

    public boolean isValida() {
        return valida;
    }

    public void setValida(boolean valida) {
        this.valida = valida;
    }

    public int getId() {
        return id;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author aapaz
 */
public class RutaParada {
    private int id;
    private int idParada;
    private int orden;

    public RutaParada(int id, int idParada, int orden) {
        this.id = id;
        this.idParada = idParada;
        this.orden = orden;
    }

   
    public int getIdRuta() {
        return id;
    }
    public int getIdParada() {
        return idParada;
    }
    public int getOrden() {
        return orden;}
}

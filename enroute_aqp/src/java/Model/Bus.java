/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author aapaz
 */
public class Bus {
    private String id;
    private String linea;
    private int capacidad;
    private double latitud;
    private double longitud;

    public Bus(String id, String linea, int capacidad) {
        this.id = id;
        this.linea = linea;
        this.capacidad = capacidad;
    }
    public Bus(){
        
    }

    public void actualizarPosicion(double lat, double lon) {
        this.latitud = lat;
        this.longitud = lon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
    
}

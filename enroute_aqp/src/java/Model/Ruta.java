/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aapaz
 */
public class Ruta {
    private int id;
    private String nombre;
    private int tiempoEstimado;

    // una ruta puede tener muchos buses y paradas
    private List<Bus> buses = new ArrayList<>();
    private List<RutaParada> paradas = new ArrayList<>();

    public Ruta(int id, String nombre, int tiempoEstimado) {
        this.id= id;
        this.nombre = nombre;
        this.tiempoEstimado = tiempoEstimado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(int tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    public List<Bus> getBuses() {
        return buses;
    }

    public void setBuses(List<Bus> buses) {
        this.buses = buses;
    }

    public List<RutaParada> getParadas() {
        return paradas;
    }

    public void setParadas(List<RutaParada> paradas) {
        this.paradas = paradas;
    }
    
}

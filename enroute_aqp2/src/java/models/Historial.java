package models;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Modelo que representa el historial de viajes de un usuario
 */
public class Historial implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int id;
    private int idUsuario;
    private int idLinea;
    private String paraderoSubida;
    private String paraderoBajada;
    private String nombreLinea;
    private int tiempoEstimado; // en minutos
    private double precio;
    private Timestamp fechaViaje;
    
    // Constructor vacío
    public Historial() {
    }
    
    // Constructor completo
    public Historial(int id, int idUsuario, int idLinea, String paraderoSubida, 
                     String paraderoBajada, String nombreLinea, int tiempoEstimado, 
                     double precio, Timestamp fechaViaje) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idLinea = idLinea;
        this.paraderoSubida = paraderoSubida;
        this.paraderoBajada = paraderoBajada;
        this.nombreLinea = nombreLinea;
        this.tiempoEstimado = tiempoEstimado;
        this.precio = precio;
        this.fechaViaje = fechaViaje;
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public int getIdLinea() {
        return idLinea;
    }
    
    public void setIdLinea(int idLinea) {
        this.idLinea = idLinea;
    }
    
    public String getParaderoSubida() {
        return paraderoSubida;
    }
    
    public void setParaderoSubida(String paraderoSubida) {
        this.paraderoSubida = paraderoSubida;
    }
    
    public String getParaderoBajada() {
        return paraderoBajada;
    }
    
    public void setParaderoBajada(String paraderoBajada) {
        this.paraderoBajada = paraderoBajada;
    }
    
    public String getNombreLinea() {
        return nombreLinea;
    }
    
    public void setNombreLinea(String nombreLinea) {
        this.nombreLinea = nombreLinea;
    }
    
    public int getTiempoEstimado() {
        return tiempoEstimado;
    }
    
    public void setTiempoEstimado(int tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }
    
    public double getPrecio() {
        return precio;
    }
    
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    public Timestamp getFechaViaje() {
        return fechaViaje;
    }
    
    public void setFechaViaje(Timestamp fechaViaje) {
        this.fechaViaje = fechaViaje;
    }
    
    @Override
    public String toString() {
        return "Historial{" +
                "id=" + id +
                ", idUsuario=" + idUsuario +
                ", idLinea=" + idLinea +
                ", paraderoSubida='" + paraderoSubida + '\'' +
                ", paraderoBajada='" + paraderoBajada + '\'' +
                ", nombreLinea='" + nombreLinea + '\'' +
                ", tiempoEstimado=" + tiempoEstimado +
                ", precio=" + precio +
                ", fechaViaje=" + fechaViaje +
                '}';
    }
}

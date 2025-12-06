package org.utp;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

/**
 * Entidad JPA para registrar el historial de viajes de los usuarios.
 * Tablla: historial_viajes
 * 
 * @author aapaz
 */
@Entity
@Table(name = "historial_viajes")
public class HistorialViajes extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_viaje")
    private int idViaje;

    // Relación ManyToOne con User (clave foránea id_usuario)
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "id_usuario", nullable = false)
    private User usuario;

    @Column(name = "id_ruta")
    private int idRuta;

    private String origen;
    private String destino;

    @Column(name = "fecha_viaje")
    private LocalDateTime fechaViaje;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos;

    @Column(name = "costo", precision = 10, scale = 2)
    private BigDecimal costo;

    @Column(name = "estado", length = 50)
    private String estado;

    @Column(name = "fecha")
    private LocalDateTime fecha; // timestamp NOT NULL DEFAULT current_timestamp()


    public HistorialViajes() {
    }

    // Getters y Setters
    public int getIdViaje() { 
        return idViaje; 
    }
    
    public void setIdViaje(int idViaje) {
        this.idViaje = idViaje; 
    }

    public User getUsuario() {
        return usuario; 
    }
    
    public void setUsuario(User usuario) { 
        this.usuario = usuario; 
    }

    public int getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(int idRuta) {
        this.idRuta = idRuta;
    }

    public String getOrigen() { 
        return origen; 
    }
    
    public void setOrigen(String origen) {
        this.origen = origen; 
    }

    public String getDestino() { 
        return destino; 
    }
    
    public void setDestino(String destino) { 
        this.destino = destino; 
    }

    public LocalDateTime getFechaViaje() {
        return fechaViaje;
    }

    public void setFechaViaje(LocalDateTime fechaViaje) {
        this.fechaViaje = fechaViaje;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFecha() { 
        return fecha; 
    }
    
    public void setFecha(LocalDateTime fecha) { 
        this.fecha = fecha; 
    }

    // Métodos Panache para queries personalizadas
    public static List<HistorialViajes> findByUsuario(int idUsuario) {
        return list("usuario.id = ?1", idUsuario);
    }
}

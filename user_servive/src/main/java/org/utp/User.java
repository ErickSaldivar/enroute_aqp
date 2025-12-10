package org.utp;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class User extends PanacheEntityBase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false)
    private int id; // Mapeado a la columna 'id_usuario' de la base de datos

    @Column(length = 100, nullable = false)
    private String nombre;

    @Column(length = 100, nullable = false)
    private String apellido;

    @Column(length = 150, nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", length = 255, nullable = false)
    private String passwordHash;

    @Column(name = "es_admin", nullable = false)
    private boolean esAdmin = false;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    public User() {}
    
    // Getters y Setters (Necesarios para Jackson y encapsulamiento)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public boolean isEsAdmin() { return esAdmin; }
    public void setEsAdmin(boolean esAdmin) { this.esAdmin = esAdmin; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }


    // Método de Negocio Panache (Usado por UserResource.java)
    public static User findByEmailAndPassword(String email, String passwordHash) {
        return find("email = ?1 and passwordHash = ?2", email, passwordHash).firstResult();
    }
}
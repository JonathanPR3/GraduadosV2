package com.example.graduados.models;
import jakarta.persistence.*;

@Entity
@Table(name = "admins")
public class admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "curp", nullable = false, length = 18)
    private String curp;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
package com.example.graduados.models;

import jakarta.persistence.*;

@Entity
@Table(name = "graduados")
public class Graduado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "curp", nullable = false, length = 18)
    private String curp;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "asistencia", nullable = false)
    private boolean asistencia;

    @Column(name = "op_titulacion", nullable = false, length = 50)
    private String opTitulacion;

    @Column(name = "asiento", length = 10)
    private String asiento;

    @Column(name = "acompanantes", nullable = false)
    private int acompanantes;

    @Column(name = "carrera", nullable = false, length = 100)
    private String carrera;

    @Column(name = "grupo", nullable = false, length = 10)
    private String grupo;

    // Nuevos campos para las claves ECDSA
    @Column(name = "clave_publica_ecdsa", columnDefinition = "TEXT")
    private String clavePublicaEcdsa;

    @Column(name = "clave_privada_ecdsa", columnDefinition = "TEXT")
    private String clavePrivadaEcdsa;

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

    public boolean isAsistencia() {
        return asistencia;
    }

    public void setAsistencia(boolean asistencia) {
        this.asistencia = asistencia;
    }

    public String getOpTitulacion() {
        return opTitulacion;
    }

    public void setOpTitulacion(String opTitulacion) {
        this.opTitulacion = opTitulacion;
    }

    public String getAsiento() {
        return asiento;
    }

    public void setAsiento(String asiento) {
        this.asiento = asiento;
    }

    public int getAcompanantes() {
        return acompanantes;
    }

    public void setAcompanantes(int acompanantes) {
        this.acompanantes = acompanantes;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getClavePublicaEcdsa() {
        return clavePublicaEcdsa;
    }

    public void setClavePublicaEcdsa(String clavePublicaEcdsa) {
        this.clavePublicaEcdsa = clavePublicaEcdsa;
    }

    public String getClavePrivadaEcdsa() {
        return clavePrivadaEcdsa;
    }

    public void setClavePrivadaEcdsa(String clavePrivadaEcdsa) {
        this.clavePrivadaEcdsa = clavePrivadaEcdsa;
    }
}

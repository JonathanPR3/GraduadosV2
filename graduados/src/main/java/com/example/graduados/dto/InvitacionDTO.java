package com.example.graduados.dto;

public class InvitacionDTO {

    private int id;
    private String curp;
    private String nombre;
    private boolean asistencia;
    private String opTitulacion;
    private String asiento;
    private int acompanantes;
    private String carrera;
    private String grupo;

    // Constructor completo
    public InvitacionDTO(int id, String curp, String nombre, boolean asistencia,
                         String opTitulacion, String asiento, int acompanantes,
                         String carrera, String grupo) {
        this.id = id;
        this.curp = curp;
        this.nombre = nombre;
        this.asistencia = asistencia;
        this.opTitulacion = opTitulacion;
        this.asiento = asiento;
        this.acompanantes = acompanantes;
        this.carrera = carrera;
        this.grupo = grupo;
    }

    // Getters y setters
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
}

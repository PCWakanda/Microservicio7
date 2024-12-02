package com.example.microservicio7.entidades;

import java.io.Serializable;

public class Estudiante implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private int tics;
    private int nota;

    public Estudiante(String nombre) {
        this.nombre = nombre;
    }

    public void incrementarTics() {
        tics++;
    }

    public int getTics() {
        return tics;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public int getNota() {
        return nota;
    }

    public String getNombre() {
        return nombre;
    }
}
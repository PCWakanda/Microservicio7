package com.example.microservicio7.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String grado;
    private int nota;
    private boolean haIniciadoSesion;

    // Constructor, getters y setters

    public Estudiante() {}

    public Estudiante(String nombre, String grado) {
        this.nombre = nombre;
        this.grado = grado;
        this.haIniciadoSesion = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public boolean isHaIniciadoSesion() {
        return haIniciadoSesion;
    }

    public void setHaIniciadoSesion(boolean haIniciadoSesion) {
        this.haIniciadoSesion = haIniciadoSesion;
    }
}
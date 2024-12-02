package com.example.microservicio7.entidades;

public class Estudiante {
    private final int id;
    private final String nombre;

    public Estudiante(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return "Estudiante{id=" + id + ", nombre='" + nombre + "'}";
    }
}
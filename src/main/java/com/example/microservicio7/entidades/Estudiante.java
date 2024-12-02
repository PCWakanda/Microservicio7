package com.example.microservicio7.entidades;

public class Estudiante {
    private final int id;

    public Estudiante(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Estudiante{id=" + id + '}';
    }
}
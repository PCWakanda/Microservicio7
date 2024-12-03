package com.example.microservicio7.controller;

import com.example.microservicio7.entidades.Estudiante;

public class EstudianteConTics {
    private final Estudiante estudiante;
    private int tics;

    public EstudianteConTics(Estudiante estudiante) {
        this.estudiante = estudiante;
        this.tics = 0;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public int getTics() {
        return tics;
    }

    public void incrementarTics() {
        this.tics++;
    }

    @Override
    public String toString() {
        return "EstudianteConTics{estudiante=" + estudiante + ", tics=" + tics + '}';
    }
}
package com.example.microservicio7.entidades;

import java.util.ArrayList;
import java.util.List;

public class Aula {
    private Long id;
    private String nombre;
    private List<Estudiante> estudiantes = new ArrayList<>();
    private static final int AFORO_MAXIMO = 15;

    public Aula(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public void agregarEstudiante(Estudiante estudiante) {
        if (estudiantes.size() < AFORO_MAXIMO) {
            estudiantes.add(estudiante);
        }
    }

    public void removerEstudiante(Estudiante estudiante) {
        estudiantes.remove(estudiante);
    }

    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public String getNombre() {
        return nombre;
    }

    public void procesarTics() {
        List<Estudiante> estudiantesASalir = new ArrayList<>();
        for (Estudiante estudiante : estudiantes) {
            estudiante.incrementarTics();
            if (estudiante.getTics() >= 3) {
                estudiante.setNota((int) (Math.random() * 10) + 1);
                estudiantesASalir.add(estudiante);
            }
        }
        estudiantes.removeAll(estudiantesASalir);
    }
}
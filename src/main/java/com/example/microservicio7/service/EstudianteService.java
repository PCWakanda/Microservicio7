package com.example.microservicio7.service;

import com.example.microservicio7.entidades.Estudiante;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class EstudianteService {
    private final Random random = new Random();
    private int contador = 0;

    public List<Estudiante> generarEstudiantes() {
        int cantidad = random.nextInt(8) + 1; // Genera un número aleatorio de estudiantes entre 1 y 8
        List<Estudiante> estudiantes = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            estudiantes.add(new Estudiante(++contador));
        }
        return estudiantes;
    }
}
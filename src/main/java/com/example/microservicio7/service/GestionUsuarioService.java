package com.example.microservicio7.service;

import com.example.microservicio7.entidades.Administrativo;
import com.example.microservicio7.entidades.Estudiante;
import com.example.microservicio7.entidades.Profesor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Random;

@Service
public class GestionUsuarioService {

    private final Map<Long, Estudiante> estudiantes = new ConcurrentHashMap<>();
    private final Map<Long, Profesor> profesores = new ConcurrentHashMap<>();
    private final Map<Long, Administrativo> administrativos = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public Mono<Estudiante> agregarEstudiante(Estudiante estudiante) {
        estudiantes.put(estudiante.getId(), estudiante);
        return Mono.just(estudiante);
    }

    public Mono<Profesor> agregarProfesor(Profesor profesor) {
        profesores.put(profesor.getId(), profesor);
        return Mono.just(profesor);
    }

    public Mono<Administrativo> agregarAdministrativo(Administrativo administrativo) {
        administrativos.put(administrativo.getId(), administrativo);
        return Mono.just(administrativo);
    }

    public Flux<Estudiante> obtenerTodosEstudiantes() {
        return Flux.fromIterable(estudiantes.values());
    }

    public Mono<Estudiante> asignarNota(Estudiante estudiante) {
        estudiante.setNota(random.nextInt(10) + 1);
        return Mono.just(estudiante);
    }

    public Mono<Estudiante> iniciarSesion(Estudiante estudiante) {
        estudiante.setHaIniciadoSesion(true);
        return Mono.just(estudiante);
    }

    public Mono<Void> entregarTarea(Estudiante estudiante, String asignatura) {
        if (estudiante.isHaIniciadoSesion()) {
            System.out.println("Estudiante " + estudiante.getNombre() + " ha entregado una tarea en " + asignatura + ".");
        }
        return Mono.empty();
    }
}
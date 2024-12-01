package com.example.microservicio7.controller;

import com.example.microservicio7.entidades.Administrativo;
import com.example.microservicio7.entidades.Estudiante;
import com.example.microservicio7.entidades.Profesor;
import com.example.microservicio7.service.GestionUsuarioService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final GestionUsuarioService gestionUsuarioService;

    public UsuarioController(GestionUsuarioService gestionUsuarioService) {
        this.gestionUsuarioService = gestionUsuarioService;
    }

    @PostMapping("/estudiantes")
    public Mono<Estudiante> crearEstudiante(@RequestBody Estudiante estudiante) {
        return gestionUsuarioService.agregarEstudiante(estudiante);
    }

    @PostMapping("/profesores")
    public Mono<Profesor> crearProfesor(@RequestBody Profesor profesor) {
        return gestionUsuarioService.agregarProfesor(profesor);
    }

    @PostMapping("/administrativos")
    public Mono<Administrativo> crearAdministrativo(@RequestBody Administrativo administrativo) {
        return gestionUsuarioService.agregarAdministrativo(administrativo);
    }

    @GetMapping("/estudiantes")
    public Flux<Estudiante> listarEstudiantes() {
        return gestionUsuarioService.obtenerTodosEstudiantes();
    }
}

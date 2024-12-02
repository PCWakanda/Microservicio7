package com.example.microservicio7.controller;

import com.example.microservicio7.entidades.Aula;
import com.example.microservicio7.service.AulaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class AulaController {
    private final AulaService aulaService;

    public AulaController(AulaService aulaService) {
        this.aulaService = aulaService;
    }

    @GetMapping("/simularAula")
    public Flux<Aula> simularAula() {
        return aulaService.simularAula();
    }
}
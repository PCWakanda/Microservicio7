package com.example.microservicio7.controller;

import com.example.microservicio7.AulaCampusFlowManager;
import com.example.microservicio7.entidades.Aula;
import com.example.microservicio7.entidades.CampusDigital;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/flujo")
public class AulaCampusController {

    private final AulaCampusFlowManager flowManager;

    public AulaCampusController(AulaCampusFlowManager flowManager) {
        this.flowManager = flowManager;
    }

    @GetMapping("/aulas")
    public Flux<Aula> flujoAulas() {
        return flowManager.obtenerFlujoAulas();
    }

    @GetMapping("/campus")
    public Flux<CampusDigital> flujoCampus() {
        return flowManager.obtenerFlujoCampus();
    }
}

package com.example.microservicio7;

import com.example.microservicio7.entidades.Aula;
import com.example.microservicio7.service.AulaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Component
public class FlujosAula {

    private static final Logger logger = LoggerFactory.getLogger(FlujosAula.class);
    private final AulaService aulaService;

    public FlujosAula(AulaService aulaService) {
        this.aulaService = aulaService;
    }

    public Flux<Aula> flujoAula() {
        return Flux.interval(Duration.ofSeconds(5))
                .flatMap(tic -> {
                    aulaService.obtenerAulas().toStream().forEach(Aula::procesarTics);
                    return aulaService.obtenerAulas();
                })
                .doOnNext(aula -> logger.info("Aula emitida: {}", aula));
    }
}
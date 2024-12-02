package com.example.microservicio7;

import com.example.microservicio7.entidades.Estudiante;
import com.example.microservicio7.service.EstudianteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@Component
public class FlujoPrincipal {
    private static final Logger logger = LoggerFactory.getLogger(FlujoPrincipal.class);
    private final EstudianteService estudianteService;
    private final Sinks.Many<Estudiante> sink = Sinks.many().multicast().onBackpressureBuffer();
    private final Map<Estudiante, EstudianteConTics> estudiantesActivos = new HashMap<>();

    public FlujoPrincipal(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    public void iniciarFlujos() {
        Flux.interval(Duration.ofSeconds(4))
            .doOnNext(tick -> {
                logger.info("---------------- tic " + tick + " ----------------");
                logger.info("-----lista estudiantes-----");
                estudiantesActivos.values().stream()
                    .sorted(Comparator.comparing(est -> est.getEstudiante().getNombre()))
                    .forEach(est -> logger.info(est.toString()));

                // Increment tics for each student and remove if tics >= 2
                estudiantesActivos.values().removeIf(est -> {
                    est.incrementarTics();
                    if (est.getTics() >= 2) {
                        logger.info("Estudiante removido: " + est.getEstudiante());
                        return true;
                    }
                    return false;
                });
            })
            .flatMap(tick -> Flux.fromIterable(estudianteService.generarEstudiantes()))
            .doOnNext(estudiante -> {
                EstudianteConTics estudianteConTics = new EstudianteConTics(estudiante);
                estudiantesActivos.put(estudiante, estudianteConTics);
                logger.info("Estudiante añadido: " + estudiante);
                sink.tryEmitNext(estudiante);
            })
            .subscribe();
    }
}
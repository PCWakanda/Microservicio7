package com.example.microservicio7;

import com.example.microservicio7.entidades.Estudiante;
import com.example.microservicio7.service.EstudianteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FlujoPrincipal {
    private static final Logger logger = LoggerFactory.getLogger(FlujoPrincipal.class);
    private final EstudianteService estudianteService;
    private final Sinks.Many<Estudiante> sink = Sinks.many().multicast().onBackpressureBuffer();
    private final Map<Estudiante, Integer> estudiantesActivos = new HashMap<>();

    public FlujoPrincipal(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    public void iniciarFlujos() {
        Flux.interval(Duration.ofSeconds(4))
            .doOnNext(tick -> logger.info("---------------- tik " + tick + " ----------------"))
            .flatMap(tick -> Flux.fromIterable(estudianteService.generarEstudiantes()))
            .doOnNext(estudiante -> {
                estudiantesActivos.put(estudiante, 0);
                logger.info("Estudiante añadido: " + estudiante);
                sink.tryEmitNext(estudiante);
            })
            .subscribe();

        sink.asFlux()
            .delayElements(Duration.ofSeconds(4))
            .doOnNext(estudiante -> {
                int ticks = estudiantesActivos.get(estudiante) + 1;
                if (ticks >= 2) {
                    estudiantesActivos.remove(estudiante);
                    logger.info("Estudiante removido: " + estudiante);
                } else {
                    estudiantesActivos.put(estudiante, ticks);
                }
            })
            .subscribe();
    }
}
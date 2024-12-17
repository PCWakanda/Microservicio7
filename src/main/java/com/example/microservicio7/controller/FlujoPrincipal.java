package com.example.microservicio7.controller;

import com.example.microservicio7.entidades.Estudiante;
import com.example.microservicio7.entidades.EstudianteRepository;
import com.example.microservicio7.service.EstudianteService;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class FlujoPrincipal {
    private static final Logger logger = LoggerFactory.getLogger(FlujoPrincipal.class);
    private final EstudianteService estudianteService;
    private final EstudianteRepository estudianteRepository;
    private final RabbitTemplate rabbitTemplate;
    private final Sinks.Many<Estudiante> sink = Sinks.many().multicast().onBackpressureBuffer();
    private final Map<Estudiante, EstudianteConTics> estudiantesActivos = new HashMap<>();
    private final Random random = new Random();

    public FlujoPrincipal(EstudianteService estudianteService, EstudianteRepository estudianteRepository, RabbitTemplate rabbitTemplate, MeterRegistry meterRegistry) {
        this.estudianteService = estudianteService;
        this.estudianteRepository = estudianteRepository;
        this.rabbitTemplate = rabbitTemplate;

        // Register a gauge to track the number of students with a grade >= 5
        meterRegistry.gauge("students.grade.gte5.count", this, FlujoPrincipal::countStudentsWithGradeGte5);
    }

    private int countStudentsWithGradeGte5() {
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        return (int) estudiantes.stream().filter(est -> est.getNota() >= 5).count();
    }

    public void iniciarFlujos() {
        // Clear the table before starting the flow
        estudianteRepository.deleteAll();
        logger.info("All records deleted from the estudiantes table.");

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
                        Estudiante estudiante = est.getEstudiante();
                        int nota = random.nextInt(10) + 0;
                        estudiante.setNota(nota);
                        if (nota == 0) {
                            estudiante.setNombre("J?");
                        }
                        estudianteRepository.save(estudiante);
                        String logMessage = "Estudiante removido: " + estudiante;
                        logger.info(logMessage);
                        rabbitTemplate.convertAndSend("aulaQueue", logMessage);
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
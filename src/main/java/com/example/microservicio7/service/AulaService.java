package com.example.microservicio7.service;

import com.example.microservicio7.entidades.Aula;
import com.example.microservicio7.entidades.Estudiante;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AulaService {
    private final List<Aula> aulas = new ArrayList<>();
    private final Random random = new Random();
    private final AtomicInteger studentCounter = new AtomicInteger(1);
    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.queue.aula}")
    private String aulaQueue;

    public AulaService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        for (int i = 0; i < 5; i++) {
            aulas.add(new Aula((long) i, "Aula " + (i + 1)));
        }
    }

    public Mono<Void> agregarEstudiante(String nombre) {
        Estudiante estudiante = new Estudiante(nombre);
        Aula aulaSeleccionada = aulas.get(random.nextInt(aulas.size()));
        aulaSeleccionada.agregarEstudiante(estudiante);
        rabbitTemplate.convertAndSend(aulaQueue, estudiante);
        return Mono.empty();
    }

    public Mono<Void> removerEstudiante(String nombre) {
        for (Aula aula : aulas) {
            Estudiante estudiante = aula.getEstudiantes().stream()
                    .filter(e -> e.getNombre().equals(nombre))
                    .findFirst()
                    .orElse(null);
            if (estudiante != null) {
                estudiante.setNota(random.nextInt(10) + 1);
                aula.removerEstudiante(estudiante);
                break;
            }
        }
        return Mono.empty();
    }

    public Flux<Aula> obtenerAulas() {
        return Flux.fromIterable(aulas);
    }

    public Flux<Aula> simularAula() {
        return Flux.interval(Duration.ofSeconds(5))
                .flatMap(tic -> {
                    if (random.nextBoolean()) {
                        String nombre = "Estudiante " + studentCounter.getAndIncrement();
                        return agregarEstudiante(nombre).thenMany(obtenerAulas());
                    } else {
                        for (Aula aula : aulas) {
                            if (!aula.getEstudiantes().isEmpty()) {
                                Estudiante estudiante = aula.getEstudiantes().get(0);
                                return removerEstudiante(estudiante.getNombre()).thenMany(obtenerAulas());
                            }
                        }
                        return obtenerAulas();
                    }
                });
    }
}
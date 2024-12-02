package com.example.microservicio7;

import com.example.microservicio7.entidades.Estudiante;
import com.example.microservicio7.service.AulaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class CampusDigital {

    private static final Logger logger = LoggerFactory.getLogger(CampusDigital.class);
    private final AulaService aulaService;
    private final Random random = new Random();
    private static final List<String> ASIGNATURAS = Arrays.asList("Biologia", "Sistemas Operativos", "Programacion", "Desarrollo App");

    public CampusDigital(AulaService aulaService) {
        this.aulaService = aulaService;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.aula}")
    public void recibirEstudiante(Estudiante estudiante) {
        logger.info("Estudiante recibido: {}", estudiante.getNombre());
        iniciarSesion(estudiante);
    }

    public void iniciarSesion(Estudiante estudiante) {
        logger.info("Estudiante {} ha iniciado sesión en el campus digital", estudiante.getNombre());
        realizarEntrega(estudiante);
    }

    public void realizarEntrega(Estudiante estudiante) {
        String asignatura = ASIGNATURAS.get(random.nextInt(ASIGNATURAS.size()));
        logger.info("Estudiante {} ha realizado una entrega en la asignatura {}", estudiante.getNombre(), asignatura);
    }
}
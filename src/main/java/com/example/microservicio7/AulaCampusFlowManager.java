package com.example.microservicio7;

import com.example.microservicio7.entidades.Aula;
import com.example.microservicio7.entidades.CampusDigital;
import com.example.microservicio7.entidades.Estudiante;
import com.example.microservicio7.service.GestionAulaCampusService;
import com.example.microservicio7.service.GestionUsuarioService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class AulaCampusFlowManager {

    private final GestionAulaCampusService gestionAulaCampusService;
    private final GestionUsuarioService gestionUsuarioService;
    private final RabbitTemplate rabbitTemplate;
    private final SimpleMessageListenerContainer listenerContainer;
    private final AtomicInteger studentCounter = new AtomicInteger(1);

    private static final String AULA_QUEUE = "aulaQueue";
    private static final String CAMPUS_QUEUE = "campusQueue";
    private static final int AFORO_MAXIMO = 20;

    public AulaCampusFlowManager(GestionAulaCampusService gestionAulaCampusService,
                                 GestionUsuarioService gestionUsuarioService,
                                 RabbitTemplate rabbitTemplate,
                                 ConnectionFactory connectionFactory) {
        this.gestionAulaCampusService = gestionAulaCampusService;
        this.gestionUsuarioService = gestionUsuarioService;
        this.rabbitTemplate = rabbitTemplate;
        this.listenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        configurarRabbitListeners();
        inicializarAulas();
    }

    private void inicializarAulas() {
        List<Aula> aulas = IntStream.range(0, 5)
                .mapToObj(i -> new Aula((long) i, "Aula " + (i + 1)))
                .collect(Collectors.toList());
        aulas.forEach(aula -> gestionAulaCampusService.agregarAula(aula).subscribe());
    }

    // Envío de mensajes a RabbitMQ
    public Mono<Void> enviarMensaje(String mensaje) {
        rabbitTemplate.convertAndSend(AULA_QUEUE, mensaje);
        return Mono.empty();
    }

    // Configuración de listeners para recibir mensajes desde RabbitMQ
    private void configurarRabbitListeners() {
        listenerContainer.setQueueNames(AULA_QUEUE, CAMPUS_QUEUE);

        listenerContainer.setMessageListener(message -> {
            // Procesar mensaje sin imprimirlo
        });

        listenerContainer.start();
    }

    // Métodos para flujos
    public Flux<Aula> obtenerFlujoAulas() {
        return gestionAulaCampusService.obtenerTodasAulas()
                .doOnNext(aula -> {
                    System.out.println("Aula emitida: " + aula);
                    enviarMensaje("Aula emitida: " + aula.getNombre()).subscribe();
                });
    }

    public Flux<CampusDigital> obtenerFlujoCampus() {
        return gestionAulaCampusService.obtenerTodosCampus();
    }

    public void gestionarFlujoAlumnos() {
        Flux.interval(Duration.ofSeconds(3))
            .doOnNext(tic -> System.out.println("TIC" + (tic + 1)))
            .flatMap(tic -> {
                int numEstudiantes = 5; // Número fijo de estudiantes por TIC
                return Flux.range(0, numEstudiantes)
                    .flatMap(i -> {
                        Estudiante estudiante = new Estudiante("Estudiante " + studentCounter.getAndIncrement(), "Grado " + (studentCounter.get() % 12));
                        return gestionAulaCampusService.obtenerTodasAulas()
                            .collectList()
                            .flatMapMany(aulas -> {
                                List<Aula> aulasDisponibles = aulas.stream()
                                    .filter(aula -> aula.getNumeroEstudiantes() < AFORO_MAXIMO)
                                    .collect(Collectors.toList());
                                if (!aulasDisponibles.isEmpty()) {
                                    Aula aulaSeleccionada = aulasDisponibles.get(studentCounter.get() % aulasDisponibles.size());
                                    aulaSeleccionada.agregarEstudiante(estudiante);
                                    String mensaje = "Estudiante " + estudiante.getNombre() + " asignado a " + aulaSeleccionada.getNombre();
                                    System.out.println(mensaje);
                                    return gestionAulaCampusService.agregarAula(aulaSeleccionada)
                                        .then(enviarMensaje(mensaje))
                                        .then(Mono.just(estudiante))
                                        .doOnNext(est -> {
                                            gestionarSalidaEstudiante(aulaSeleccionada, est, tic + 1);
                                            iniciarSesion(est);
                                        });
                                } else {
                                    return Mono.empty();
                                }
                            });
                    });
            })
            .subscribeOn(Schedulers.boundedElastic())
            .subscribe();
    }

    private void gestionarSalidaEstudiante(Aula aula, Estudiante estudiante, long ticEntrada) {
        Flux.interval(Duration.ofSeconds(3))
            .filter(tic -> tic == ticEntrada + 3)
            .take(1)
            .doOnNext(tic -> {
                aula.removerEstudiante(estudiante);
                gestionUsuarioService.asignarNota(estudiante).subscribe();
                System.out.println("Estudiante " + estudiante.getNombre() + " salió de " + aula.getNombre() + " con nota: " + estudiante.getNota());
            })
            .subscribe();
    }

    private void iniciarSesion(Estudiante estudiante) {
        if (Math.random() > 0.5) { // Simulación de decisión de iniciar sesión
            estudiante.setHaIniciadoSesion(true);
            System.out.println("Estudiante " + estudiante.getNombre() + " ha iniciado sesión en el campus virtual.");
            entregarTarea(estudiante);
        }
    }

    private void entregarTarea(Estudiante estudiante) {
        if (estudiante.isHaIniciadoSesion()) {
            String[] asignaturas = {"Sistemas Operativos", "Programación Concurrente", "Rubenología", "Matemáticas"};
            String asignatura = asignaturas[(int) (Math.random() * asignaturas.length)];
            System.out.println("Estudiante " + estudiante.getNombre() + " ha entregado una tarea en " + asignatura + ".");
        }
    }
}
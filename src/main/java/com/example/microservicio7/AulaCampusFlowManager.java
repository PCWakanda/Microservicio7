package com.example.microservicio7;

import com.example.microservicio7.entidades.Aula;
import com.example.microservicio7.entidades.CampusDigital;
import com.example.microservicio7.service.GestionAulaCampusService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class AulaCampusFlowManager {

    private final GestionAulaCampusService gestionAulaCampusService;
    private final RabbitTemplate rabbitTemplate;
    private final SimpleMessageListenerContainer listenerContainer;

    private static final String AULA_QUEUE = "aulaQueue";
    private static final String CAMPUS_QUEUE = "campusQueue";

    public AulaCampusFlowManager(GestionAulaCampusService gestionAulaCampusService,
                                 RabbitTemplate rabbitTemplate,
                                 ConnectionFactory connectionFactory) {
        this.gestionAulaCampusService = gestionAulaCampusService;
        this.rabbitTemplate = rabbitTemplate;
        this.listenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        configurarRabbitListeners();
    }

    // Envío de mensajes a RabbitMQ
    public Mono<Void> enviarAulaCreada(Aula aula) {
        rabbitTemplate.convertAndSend(AULA_QUEUE, aula);
        return Mono.empty();
    }

    public Mono<Void> enviarCampusCreado(CampusDigital campusDigital) {
        rabbitTemplate.convertAndSend(CAMPUS_QUEUE, campusDigital);
        return Mono.empty();
    }

    // Configuración de listeners para recibir mensajes desde RabbitMQ
    private void configurarRabbitListeners() {
        listenerContainer.setQueueNames(AULA_QUEUE, CAMPUS_QUEUE);

        listenerContainer.setMessageListener(message -> {
            String receivedMessage = new String(message.getBody());
            System.out.println("Mensaje recibido: " + receivedMessage);
            // Procesar mensaje
        });

        listenerContainer.start();
    }

    // Métodos para flujos
    public Flux<Aula> obtenerFlujoAulas() {
        return gestionAulaCampusService.obtenerTodasAulas()
                .doOnNext(aula -> enviarAulaCreada(aula).subscribe());
    }

    public Flux<CampusDigital> obtenerFlujoCampus() {
        return gestionAulaCampusService.obtenerTodosCampus()
                .doOnNext(campus -> enviarCampusCreado(campus).subscribe());
    }

    @Bean
    public Queue aulaQueue() {
        return new Queue(AULA_QUEUE);
    }

    @Bean
    public Queue campusQueue() {
        return new Queue(CAMPUS_QUEUE);
    }
}

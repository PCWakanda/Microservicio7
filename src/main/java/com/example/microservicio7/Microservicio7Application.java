package com.example.microservicio7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@EnableDiscoveryClient
public class Microservicio7Application {

    private final AulaCampusFlowManager flowManager;

    public Microservicio7Application(AulaCampusFlowManager flowManager) {
        this.flowManager = flowManager;
    }

    public static void main(String[] args) {
        SpringApplication.run(Microservicio7Application.class, args);
    }

    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        flowManager.obtenerFlujoAulas().subscribe();
        flowManager.gestionarFlujoAlumnos(); // Llamada al método para gestionar el flujo de alumnos
    }
}
package com.example.microservicio7;

import com.example.microservicio7.entidades.Aula;
import com.example.microservicio7.service.AulaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class FlujoPrincipal {

    private static final Logger logger = LoggerFactory.getLogger(FlujoPrincipal.class);
    private final AulaService aulaService;
    private final CampusDigital campusDigital;

    public FlujoPrincipal(AulaService aulaService, CampusDigital campusDigital) {
        this.aulaService = aulaService;
        this.campusDigital = campusDigital;
    }

    public Flux<Aula> flujoAula() {
        return aulaService.simularAula()
                .doOnNext(aula -> logger.info("Aula emitida: {}", aula));
    }

    public void iniciarFlujos() {
        flujoAula().subscribe();
        // Aquí puedes agregar cualquier lógica adicional para iniciar el flujo de CampusDigital si es necesario
    }
}
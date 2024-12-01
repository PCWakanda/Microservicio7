package com.example.microservicio7.service;

import com.example.microservicio7.entidades.Aula;
import com.example.microservicio7.entidades.CampusDigital;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class GestionAulaCampusService {

    private final Map<Long, Aula> aulas = new ConcurrentHashMap<>();
    private final Map<Long, CampusDigital> campus = new ConcurrentHashMap<>();

    public Mono<Aula> agregarAula(Aula aula) {
        aulas.put(aula.getId(), aula);
        return Mono.just(aula);
    }

    public Mono<CampusDigital> agregarCampus(CampusDigital campusDigital) {
        campus.put(campusDigital.getId(), campusDigital);
        return Mono.just(campusDigital);
    }

    public Flux<Aula> obtenerTodasAulas() {
        return Flux.fromIterable(aulas.values());
    }

    public Flux<CampusDigital> obtenerTodosCampus() {
        return Flux.fromIterable(campus.values());
    }
}

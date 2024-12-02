package com.example.microservicio7.entidades;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "campus_digital")
public class CampusDigital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String ubicacionVirtual;

    @OneToMany
    private List<Asignatura> asignaturas;

    public CampusDigital() {}

    public CampusDigital(String nombre, String ubicacionVirtual, List<Asignatura> asignaturas) {
        this.nombre = nombre;
        this.ubicacionVirtual = ubicacionVirtual;
        this.asignaturas = asignaturas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacionVirtual() {
        return ubicacionVirtual;
    }

    public void setUbicacionVirtual(String ubicacionVirtual) {
        this.ubicacionVirtual = ubicacionVirtual;
    }

    public List<Asignatura> getAsignaturas() {
        return asignaturas;
    }

    public void setAsignaturas(List<Asignatura> asignaturas) {
        this.asignaturas = asignaturas;
    }
}
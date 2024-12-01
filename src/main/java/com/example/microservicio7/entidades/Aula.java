package com.example.microservicio7.entidades;

import jakarta.persistence.*;


import java.util.HashSet;
import java.util.Set;

@Entity
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Estudiante> estudiantes = new HashSet<>();

    // Constructor, getters y setters

    public Aula() {}

    public Aula(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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

    public Set<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public void setEstudiantes(Set<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }

    public void agregarEstudiante(Estudiante estudiante) {
        this.estudiantes.add(estudiante);
    }

    public void removerEstudiante(Estudiante estudiante) {
        this.estudiantes.remove(estudiante);
    }

    public int getNumeroEstudiantes() {
        return this.estudiantes.size();
    }
}
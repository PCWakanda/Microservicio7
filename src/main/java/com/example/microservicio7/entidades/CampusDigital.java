package com.example.microservicio7.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "campus_digital")
public class CampusDigital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String ubicacionVirtual;

    public CampusDigital() {}

    public CampusDigital(String nombre, String ubicacionVirtual) {
        this.nombre = nombre;
        this.ubicacionVirtual = ubicacionVirtual;
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
}

package com.alura.challenge.Literatura_ChallengeOracle.model;

import jakarta.persistence.*;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombre;
    private Integer diaCumpleanos;
    private Integer diaFallecio;

    public Autor() {}

    public Autor(DatosAutor datosAutor) {
        this.nombre = datosAutor.name();
        this.diaCumpleanos = datosAutor.birthday();
        this.diaFallecio = datosAutor.deathday();
    }

    // Getters y Setters
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

    public Integer getDiaCumpleanos() {
        return diaCumpleanos;
    }

    public void setDiaCumpleanos(Integer diaCumpleanos) {
        this.diaCumpleanos = diaCumpleanos;
    }

    public Integer getDiaFallecio() {
        return diaFallecio;
    }

    public void setDiaFallecio(Integer diaFallecio) {
        this.diaFallecio = diaFallecio;
    }
}

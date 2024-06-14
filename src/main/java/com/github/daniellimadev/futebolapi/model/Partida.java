package com.github.daniellimadev.futebolapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private Clube clubeCasa;

    @ManyToOne
    @NotNull
    private Clube clubeVisitante;

    @NotNull
    @Min(0)
    private Integer golsCasa;

    @NotNull
    @Min(0)
    private Integer golsVisitante;

    @ManyToOne
    @NotNull
    private Estadio estadio;

    @NotNull
    @FutureOrPresent
    private LocalDateTime dataHora;


    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull Clube getClubeCasa() {
        return clubeCasa;
    }

    public void setClubeCasa(@NotNull Clube clubeCasa) {
        this.clubeCasa = clubeCasa;
    }

    public @NotNull Clube getClubeVisitante() {
        return clubeVisitante;
    }

    public void setClubeVisitante(@NotNull Clube clubeVisitante) {
        this.clubeVisitante = clubeVisitante;
    }

    public @NotNull @Min(0) Integer getGolsCasa() {
        return golsCasa;
    }

    public void setGolsCasa(@NotNull @Min(0) Integer golsCasa) {
        this.golsCasa = golsCasa;
    }

    public @NotNull @Min(0) Integer getGolsVisitante() {
        return golsVisitante;
    }

    public void setGolsVisitante(@NotNull @Min(0) Integer golsVisitante) {
        this.golsVisitante = golsVisitante;
    }

    public @NotNull Estadio getEstadio() {
        return estadio;
    }

    public void setEstadio(@NotNull Estadio estadio) {
        this.estadio = estadio;
    }

    public @NotNull @FutureOrPresent LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(@NotNull @FutureOrPresent LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}



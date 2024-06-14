package com.github.daniellimadev.futebolapi.repository;

import com.github.daniellimadev.futebolapi.model.Clube;
import com.github.daniellimadev.futebolapi.model.Estadio;
import com.github.daniellimadev.futebolapi.model.Partida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PartidaRepository extends JpaRepository<Partida, Long> {

    @Query("SELECT p FROM Partida p WHERE (p.clubeCasa = :clube OR p.clubeVisitante = :clube) AND p.dataHora BETWEEN :start AND :end")
    List<Partida> findPartidasWithinTimeFrame(@Param("clube") Clube clube, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT p FROM Partida p WHERE p.estadio = :estadio AND DATE(p.dataHora) = DATE(:dataHora)")
    List<Partida> findPartidasByEstadioAndDataHora(@Param("estadio") Estadio estadio, @Param("dataHora") LocalDateTime dataHora);

    @Query("SELECT p FROM Partida p WHERE (:clube IS NULL OR p.clubeCasa = :clube OR p.clubeVisitante = :clube) AND (:estadio IS NULL OR p.estadio = :estadio)")
    Page<Partida> findByClubeOrEstadio(@Param("clube") Clube clube, @Param("estadio") Estadio estadio, Pageable pageable);
}


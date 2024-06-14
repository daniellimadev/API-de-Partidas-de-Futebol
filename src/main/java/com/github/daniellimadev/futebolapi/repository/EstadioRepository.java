package com.github.daniellimadev.futebolapi.repository;

import com.github.daniellimadev.futebolapi.model.Estadio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadioRepository extends JpaRepository<Estadio, Long> {
    Optional<Estadio> findByNome(String nome);
    Page<Estadio> findAll(Pageable pageable);
}

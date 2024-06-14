package com.github.daniellimadev.futebolapi.repository;

import com.github.daniellimadev.futebolapi.model.Clube;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClubeRepository extends JpaRepository<Clube, Long> {
    Optional<Clube> findByNomeAndEstado(String nome, String estado);

    Page<Clube> findByNomeContaining(String nome, Pageable pageable);
    Page<Clube> findByEstado(String estado, Pageable pageable);
    Page<Clube> findByAtivo(Boolean ativo, Pageable pageable);
}

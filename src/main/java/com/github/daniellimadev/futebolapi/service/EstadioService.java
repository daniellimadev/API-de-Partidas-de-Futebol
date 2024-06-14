package com.github.daniellimadev.futebolapi.service;

import com.github.daniellimadev.futebolapi.model.Estadio;
import com.github.daniellimadev.futebolapi.repository.EstadioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
public class EstadioService {

    @Autowired
    private EstadioRepository estadioRepository;

    public Estadio cadastrarEstadio(@Valid Estadio estadio) {
        if (estadioRepository.findByNome(estadio.getNome()).isPresent()) {
            throw new RuntimeException("Estádio já existe.");
        }
        return estadioRepository.save(estadio);
    }

    public Estadio editarEstadio(Long id, @Valid Estadio estadioAtualizado) {
        Estadio estadio = estadioRepository.findById(id).orElseThrow(() -> new RuntimeException("Estádio não encontrado."));

        Optional<Estadio> existente = estadioRepository.findByNome(estadioAtualizado.getNome());
        if (existente.isPresent() && !existente.get().getId().equals(id)) {
            throw new RuntimeException("Estádio já existe.");
        }

        estadio.setNome(estadioAtualizado.getNome());
        return estadioRepository.save(estadio);
    }

    public Estadio buscarEstadioPorId(Long id) {
        return estadioRepository.findById(id).orElseThrow(() -> new RuntimeException("Estádio não encontrado."));
    }

    public Page<Estadio> listarEstadios(Pageable pageable) {
        return estadioRepository.findAll(pageable);
    }
}

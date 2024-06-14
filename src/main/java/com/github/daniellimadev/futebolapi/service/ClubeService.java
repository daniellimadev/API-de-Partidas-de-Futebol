package com.github.daniellimadev.futebolapi.service;

import com.github.daniellimadev.futebolapi.model.Clube;
import com.github.daniellimadev.futebolapi.repository.ClubeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
public class ClubeService {

    @Autowired
    private ClubeRepository clubeRepository;

    public Clube cadastrarClube(@Valid Clube clube) {
        Optional<Clube> existente = clubeRepository.findByNomeAndEstado(clube.getNome(), clube.getEstado());
        if (existente.isPresent()) {
            throw new RuntimeException("Clube já existe.");
        }
        return clubeRepository.save(clube);
    }

    public Clube editarClube(Long id, @Valid Clube clubeAtualizado) {
        Clube clube = clubeRepository.findById(id).orElseThrow(() -> new RuntimeException("Clube não encontrado."));
        if (!clube.getNome().equals(clubeAtualizado.getNome()) || !clube.getEstado().equals(clubeAtualizado.getEstado())) {
            Optional<Clube> existente = clubeRepository.findByNomeAndEstado(clubeAtualizado.getNome(), clubeAtualizado.getEstado());
            if (existente.isPresent()) {
                throw new RuntimeException("Conflito de dados.");
            }
        }
        clube.setNome(clubeAtualizado.getNome());
        clube.setEstado(clubeAtualizado.getEstado());
        clube.setDataCriacao(clubeAtualizado.getDataCriacao());
        clube.setAtivo(clubeAtualizado.isAtivo());
        return clubeRepository.save(clube);
    }

    public void inativarClube(Long id) {
        Clube clube = clubeRepository.findById(id).orElseThrow(() -> new RuntimeException("Clube não encontrado."));
        clube.setAtivo(false);
        clubeRepository.save(clube);
    }

    public Clube buscarClubePorId(Long id) {
        return clubeRepository.findById(id).orElseThrow(() -> new RuntimeException("Clube não encontrado."));
    }

    public Page<Clube> listarClubes(String nome, String estado, Boolean ativo, Pageable pageable) {
        if (nome != null) {
            return clubeRepository.findByNomeContaining(nome, pageable);
        } else if (estado != null) {
            return clubeRepository.findByEstado(estado, pageable);
        } else if (ativo != null) {
            return clubeRepository.findByAtivo(ativo, pageable);
        } else {
            return clubeRepository.findAll(pageable);
        }
    }
}
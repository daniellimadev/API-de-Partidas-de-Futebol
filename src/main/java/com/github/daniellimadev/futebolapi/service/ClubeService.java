package com.github.daniellimadev.futebolapi.service;

import com.github.daniellimadev.futebolapi.model.Clube;
import com.github.daniellimadev.futebolapi.repository.ClubeRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@Validated
public class ClubeService {

    @Autowired
    private ClubeRepository clubeRepository;

    public Clube cadastrarClube(@Valid Clube clube) {
        validateClube(clube);

        Optional<Clube> existente = clubeRepository.findByNomeAndEstado(clube.getNome(), clube.getEstado());
        if (existente.isPresent()) {
            throw new RuntimeException("Clube já existe.");
        }
        return clubeRepository.save(clube);
    }

    public Clube editarClube(Long id, @Valid Clube clubeAtualizado) {
        validateClube(clubeAtualizado);

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


    private void validateClube(Clube clube) {
        if (clube.getNome() == null || clube.getNome().length() < 2) {
            log.error("Nome inválido!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (clube.getEstado() == null || !isValidEstado(clube.getEstado())) {
            log.error("Estado inválido!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (clube.getDataCriacao() == null || clube.getDataCriacao().isAfter(LocalDate.now())) {
            log.error("Data de criação inválida");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isValidEstado(String siglaEstado) {
        String[] estados = {"AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"};
        for (String estado : estados) {
            if (estado.equals(siglaEstado)) {
                return true;
            }
        }
        return false;
    }

}
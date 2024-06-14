package com.github.daniellimadev.futebolapi.service;

import com.github.daniellimadev.futebolapi.model.Clube;
import com.github.daniellimadev.futebolapi.model.Estadio;
import com.github.daniellimadev.futebolapi.model.Partida;
import com.github.daniellimadev.futebolapi.repository.ClubeRepository;
import com.github.daniellimadev.futebolapi.repository.EstadioRepository;
import com.github.daniellimadev.futebolapi.repository.PartidaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class PartidaService {

    @Autowired
    private PartidaRepository partidaRepository;

    @Autowired
    private ClubeRepository clubeRepository;

    @Autowired
    private EstadioRepository estadioRepository;

    public Partida cadastrarPartida(@Valid Partida partida) {
        validarPartida(partida);
        return partidaRepository.save(partida);
    }

    public Partida editarPartida(Long id, @Valid Partida partidaAtualizada) {
        Partida partida = partidaRepository.findById(id).orElseThrow(() -> new RuntimeException("Partida não encontrada."));
        validarPartida(partidaAtualizada);

        partida.setClubeCasa(partidaAtualizada.getClubeCasa());
        partida.setClubeVisitante(partidaAtualizada.getClubeVisitante());
        partida.setGolsCasa(partidaAtualizada.getGolsCasa());
        partida.setGolsVisitante(partidaAtualizada.getGolsVisitante());
        partida.setEstadio(partidaAtualizada.getEstadio());
        partida.setDataHora(partidaAtualizada.getDataHora());

        return partidaRepository.save(partida);
    }

    public void removerPartida(Long id) {
        Partida partida = partidaRepository.findById(id).orElseThrow(() -> new RuntimeException("Partida não encontrada."));
        partidaRepository.delete(partida);
    }

    public Partida buscarPartidaPorId(Long id) {
        return partidaRepository.findById(id).orElseThrow(() -> new RuntimeException("Partida não encontrada."));
    }

    public Page<Partida> listarPartidas(Long clubeId, Long estadioId, Pageable pageable) {
        Clube clube = clubeId != null ? clubeRepository.findById(clubeId).orElse(null) : null;
        Estadio estadio = estadioId != null ? estadioRepository.findById(estadioId).orElse(null) : null;
        return partidaRepository.findByClubeOrEstadio(clube, estadio, pageable);
    }

    private void validarPartida(Partida partida) {
        if (partida.getClubeCasa().getId().equals(partida.getClubeVisitante().getId())) {
            throw new RuntimeException("Os clubes da casa e visitante devem ser diferentes.");
        }

        Clube clubeCasa = clubeRepository.findById(partida.getClubeCasa().getId())
                .orElseThrow(() -> new RuntimeException("Clube da casa não encontrado."));
        Clube clubeVisitante = clubeRepository.findById(partida.getClubeVisitante().getId())
                .orElseThrow(() -> new RuntimeException("Clube visitante não encontrado."));

        if (!clubeCasa.isAtivo() || !clubeVisitante.isAtivo()) {
            throw new RuntimeException("Clube inativo.");
        }


        Estadio estadio = estadioRepository.findById(partida.getEstadio().getId())
                .orElseThrow(() -> new RuntimeException("Estádio não encontrado."));

        List<Partida> partidasProximas = partidaRepository.findPartidasWithinTimeFrame(clubeCasa, partida.getDataHora().minusHours(48), partida.getDataHora().plusHours(48));
        partidasProximas.addAll(partidaRepository.findPartidasWithinTimeFrame(clubeVisitante, partida.getDataHora().minusHours(48), partida.getDataHora().plusHours(48)));
        if (!partidasProximas.isEmpty()) {
            throw new RuntimeException("Um dos clubes já possui uma partida marcada com diferença menor do que 48 horas.");
        }

        List<Partida> partidasNoEstadio = partidaRepository.findPartidasByEstadioAndDataHora(estadio, partida.getDataHora());
        if (!partidasNoEstadio.isEmpty()) {
            throw new RuntimeException("O estádio já possui uma partida cadastrada para o mesmo dia.");
        }
    }
}
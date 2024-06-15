package com.github.daniellimadev.futebolapi.controller;

import com.github.daniellimadev.futebolapi.model.Clube;
import com.github.daniellimadev.futebolapi.model.Partida;
import com.github.daniellimadev.futebolapi.repository.ClubeRepository;
import com.github.daniellimadev.futebolapi.service.PartidaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/partidas")
public class PartidaController {

    @Autowired
    private PartidaService partidaService;

    @Autowired
    private ClubeRepository clubeRepository;

    @PostMapping
    public ResponseEntity<Partida> cadastrarPartida(@Valid @RequestBody Partida partida) {
        Partida novaPartida = partidaService.cadastrarPartida(partida);
        return new ResponseEntity<>(novaPartida, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Partida> editarPartida(@PathVariable Long id, @Valid @RequestBody Partida partida) {
        Partida partidaAtualizada = partidaService.editarPartida(id, partida);
        return new ResponseEntity<>(partidaAtualizada, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerPartida(@PathVariable Long id) {
        partidaService.removerPartida(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Partida> buscarPartidaPorId(@PathVariable Long id) {
        Partida partida = partidaService.buscarPartidaPorId(id);
        return new ResponseEntity<>(partida, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Partida>> listarPartidas(
            @RequestParam(required = false) Long clubeId,
            @RequestParam(required = false) Long estadioId,
            Pageable pageable) {
        Page<Partida> partidas = partidaService.listarPartidas(clubeId, estadioId, pageable);
        return new ResponseEntity<>(partidas, HttpStatus.OK);
    }

    @GetMapping("/retrospecto/{clubeId}")
    public ResponseEntity<?> retrospectoGeral(@PathVariable Long clubeId) {
        Clube clube = clubeRepository.findById(clubeId).orElse(null);
        if (clube == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Map<String, Integer> retrospecto = partidaService.calcularRetrospectoGeral(clube);
        return new ResponseEntity<>(retrospecto, HttpStatus.OK);
    }

    @GetMapping("/retrospecto/adversarios/{clubeId}")
    public ResponseEntity<?> retrospectoContraAdversarios(@PathVariable Long clubeId) {
        Clube clube = clubeRepository.findById(clubeId).orElse(null);
        if (clube == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Map<Clube, Map<String, Integer>> retrospecto = partidaService.calcularRetrospectoContraAdversarios(clube);
        return new ResponseEntity<>(retrospecto, HttpStatus.OK);
    }

    @GetMapping("/confrontos/{clube1Id}/{clube2Id}")
    public ResponseEntity<?> confrontosDiretos(@PathVariable Long clube1Id, @PathVariable Long clube2Id) {
        Clube clube1 = clubeRepository.findById(clube1Id).orElse(null);
        Clube clube2 = clubeRepository.findById(clube2Id).orElse(null);
        if (clube1 == null || clube2 == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Map<String, Object> confrontos = partidaService.calcularConfrontoDireto(clube1, clube2);
        return new ResponseEntity<>(confrontos, HttpStatus.OK);
    }

    @GetMapping("/ranking")
    public ResponseEntity<?> ranking(@RequestParam String tipo) {
        try {
            List<Map<String, Object>> ranking = partidaService.calcularRanking(tipo);
            return new ResponseEntity<>(ranking, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/listar/filtros")
    public ResponseEntity<?> listarPartidasComFiltros(
            @RequestParam(required = false) Long clubeId,
            @RequestParam(required = false) Boolean goleadas,
            @RequestParam(required = false) Boolean mandante,
            @RequestParam(required = false) Boolean visitante) {
        List<Partida> partidas = partidaService.listarPartidasComFiltros(clubeId, goleadas, mandante, visitante);
        return new ResponseEntity<>(partidas, HttpStatus.OK);
    }

    @GetMapping("/ranking/filtros")
    public ResponseEntity<?> ranking(
            @RequestParam String tipo,
            @RequestParam(required = false) Boolean mandante,
            @RequestParam(required = false) Boolean visitante) {
        List<Map<String, Object>> ranking = partidaService.calcularRankingComFiltros(tipo, mandante, visitante);
        return new ResponseEntity<>(ranking, HttpStatus.OK);
    }

}

package com.github.daniellimadev.futebolapi.controller;

import com.github.daniellimadev.futebolapi.model.Partida;
import com.github.daniellimadev.futebolapi.service.PartidaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/partidas")
public class PartidaController {

    @Autowired
    private PartidaService partidaService;

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
}

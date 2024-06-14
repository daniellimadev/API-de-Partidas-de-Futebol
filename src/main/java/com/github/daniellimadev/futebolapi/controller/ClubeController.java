package com.github.daniellimadev.futebolapi.controller;

import com.github.daniellimadev.futebolapi.model.Clube;
import com.github.daniellimadev.futebolapi.service.ClubeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clubes")
public class ClubeController {

    @Autowired
    private ClubeService clubeService;

    @PostMapping
    public ResponseEntity<Clube> cadastrarClube(@Valid @RequestBody Clube clube) {
        Clube novoClube = clubeService.cadastrarClube(clube);
        return new ResponseEntity<>(novoClube, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Clube> editarClube(@PathVariable Long id, @Valid @RequestBody Clube clube) {
        Clube clubeAtualizado = clubeService.editarClube(id, clube);
        return new ResponseEntity<>(clubeAtualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativarClube(@PathVariable Long id) {
        clubeService.inativarClube(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Clube> buscarClubePorId(@PathVariable Long id) {
        Clube clube = clubeService.buscarClubePorId(id);
        return new ResponseEntity<>(clube, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Clube>> listarClubes(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Boolean ativo,
            Pageable pageable) {
        Page<Clube> clubes = clubeService.listarClubes(nome, estado, ativo, pageable);
        return new ResponseEntity<>(clubes, HttpStatus.OK);
    }
}

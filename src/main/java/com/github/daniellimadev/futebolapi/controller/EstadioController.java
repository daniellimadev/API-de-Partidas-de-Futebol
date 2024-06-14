package com.github.daniellimadev.futebolapi.controller;

import com.github.daniellimadev.futebolapi.model.Estadio;
import com.github.daniellimadev.futebolapi.service.EstadioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estadios")
public class EstadioController {

    @Autowired
    private EstadioService estadioService;

    @PostMapping
    public ResponseEntity<Estadio> cadastrarEstadio(@Valid @RequestBody Estadio estadio) {
        Estadio novoEstadio = estadioService.cadastrarEstadio(estadio);
        return new ResponseEntity<>(novoEstadio, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estadio> editarEstadio(@PathVariable Long id, @Valid @RequestBody Estadio estadio) {
        Estadio estadioAtualizado = estadioService.editarEstadio(id, estadio);
        return new ResponseEntity<>(estadioAtualizado, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estadio> buscarEstadioPorId(@PathVariable Long id) {
        Estadio estadio = estadioService.buscarEstadioPorId(id);
        return new ResponseEntity<>(estadio, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Estadio>> listarEstadios(Pageable pageable) {
        Page<Estadio> estadios = estadioService.listarEstadios(pageable);
        return new ResponseEntity<>(estadios, HttpStatus.OK);
    }
}
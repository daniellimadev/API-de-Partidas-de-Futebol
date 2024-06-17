package com.github.daniellimadev.futebolapi.controller;

import com.github.daniellimadev.futebolapi.model.Estadio;
import com.github.daniellimadev.futebolapi.service.EstadioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Estádio:")
@RequestMapping("/estadios")
public class EstadioController {

    @Autowired
    private EstadioService estadioService;

    @Operation(summary = "Cadastrar um estádio", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Estádio criado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Dados de requisição inválida!"),
            @ApiResponse(responseCode = "409", description = "Estádio já existe"),
            @ApiResponse(responseCode = "500", description = "Serviço indisponível!"),
    })
    @PostMapping
    public ResponseEntity<Estadio> cadastrarEstadio(@Valid @RequestBody Estadio estadio) {
        Estadio novoEstadio = estadioService.cadastrarEstadio(estadio);
        return new ResponseEntity<>(novoEstadio, HttpStatus.CREATED);
    }

    @Operation(summary = "Editar um estádio", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Estádio editado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Dados de requisição inválida!"),
            @ApiResponse(responseCode = "409", description = "Estádio já existe!"),
            @ApiResponse(responseCode = "404", description = "Estádio não existe!"),
            @ApiResponse(responseCode = "500", description = "Serviço indisponível!"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Estadio> editarEstadio(@PathVariable Long id, @Valid @RequestBody Estadio estadio) {
        Estadio estadioAtualizado = estadioService.editarEstadio(id, estadio);
        return new ResponseEntity<>(estadioAtualizado, HttpStatus.OK);
    }

    @Operation(summary = "Buscar um estádio pelo seu ID", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca pelo estádio com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Dados de requisição inválida!"),
            @ApiResponse(responseCode = "404", description = "Busca sem resultado!"),
            @ApiResponse(responseCode = "500", description = "Serviço indisponível!"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Estadio> buscarEstadioPorId(@PathVariable Long id) {
        Estadio estadio = estadioService.buscarEstadioPorId(id);
        return new ResponseEntity<>(estadio, HttpStatus.OK);
    }

    @Operation(summary = "Listar todos os estádios", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listando todos os estádios com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Serviço indisponível!"),
    })
    @GetMapping
    public ResponseEntity<Page<Estadio>> listarEstadios(Pageable pageable) {
        Page<Estadio> estadios = estadioService.listarEstadios(pageable);
        return new ResponseEntity<>(estadios, HttpStatus.OK);
    }
}
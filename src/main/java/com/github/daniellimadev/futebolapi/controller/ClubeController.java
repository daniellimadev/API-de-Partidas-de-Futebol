package com.github.daniellimadev.futebolapi.controller;

import com.github.daniellimadev.futebolapi.model.Clube;
import com.github.daniellimadev.futebolapi.service.ClubeService;
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
@Tag(name = "Clube:")
@RequestMapping("/clubes")
public class ClubeController {

    @Autowired
    private ClubeService clubeService;

    @Operation(summary = "Cria uma Clube", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Clube criado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Dados de requisição inválida!"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados!"),
            @ApiResponse(responseCode = "500", description = "Serviço indisponível!"),
    })
    @PostMapping
    public ResponseEntity<Clube> cadastrarClube(@Valid @RequestBody Clube clube) {
        Clube novoClube = clubeService.cadastrarClube(clube);
        return new ResponseEntity<>(novoClube, HttpStatus.CREATED);
    }

    @Operation(summary = "Editar um clube", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clube criado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Dados de requisição inválida!"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados!"),
            @ApiResponse(responseCode = "404", description = "Clube não existe!"),
            @ApiResponse(responseCode = "500", description = "Serviço indisponível!"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Clube> editarClube(@PathVariable Long id, @Valid @RequestBody Clube clube) {
        Clube clubeAtualizado = clubeService.editarClube(id, clube);
        return new ResponseEntity<>(clubeAtualizado, HttpStatus.OK);
    }

    @Operation(summary = "Inativar um clube", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Inativação do Clube com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Clube não existe!"),
            @ApiResponse(responseCode = "500", description = "Serviço indisponível!"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativarClube(@PathVariable Long id) {
        clubeService.inativarClube(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Buscar um clube  pelo seu ID", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = " Buscar um Clube com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Busca sem resultado!"),
            @ApiResponse(responseCode = "500", description = "Serviço indisponível!"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Clube> buscarClubePorId(@PathVariable Long id) {
        Clube clube = clubeService.buscarClubePorId(id);
        return new ResponseEntity<>(clube, HttpStatus.OK);
    }

    @Operation(summary = "Listar clubes", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Listando todos os Clubes com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Serviço indisponível!"),
    })
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

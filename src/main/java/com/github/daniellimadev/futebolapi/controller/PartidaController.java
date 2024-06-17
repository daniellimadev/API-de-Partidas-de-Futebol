package com.github.daniellimadev.futebolapi.controller;

import com.github.daniellimadev.futebolapi.model.Clube;
import com.github.daniellimadev.futebolapi.model.Partida;
import com.github.daniellimadev.futebolapi.repository.ClubeRepository;
import com.github.daniellimadev.futebolapi.service.PartidaService;
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

import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Partida:")
@RequestMapping("/partidas")
public class PartidaController {

    @Autowired
    private PartidaService partidaService;

    @Autowired
    private ClubeRepository clubeRepository;

    @Operation(summary = "Cadastrar uma partida", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Partida criado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Dados de requisição inválida!"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados!"),
            @ApiResponse(responseCode = "500", description = "Serviço indisponível!"),
    })
    @PostMapping
    public ResponseEntity<Partida> cadastrarPartida(@Valid @RequestBody Partida partida) {
        Partida novaPartida = partidaService.cadastrarPartida(partida);
        return new ResponseEntity<>(novaPartida, HttpStatus.CREATED);
    }

    @Operation(summary = "Editar uma partida", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partida criado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Dados de requisição inválida!"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados!"),
            @ApiResponse(responseCode = "404", description = "Partida não existe!"),
            @ApiResponse(responseCode = "500", description = "Serviço indisponível!"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Partida> editarPartida(@PathVariable Long id, @Valid @RequestBody Partida partida) {
        Partida partidaAtualizada = partidaService.editarPartida(id, partida);
        return new ResponseEntity<>(partidaAtualizada, HttpStatus.OK);
    }

    @Operation(summary = "Remover uma partida", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Partida criado com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Partida não existe!"),
            @ApiResponse(responseCode = "500", description = "Serviço indisponível!"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerPartida(@PathVariable Long id) {
        partidaService.removerPartida(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Buscar Partida por Id", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Buscar Partida com sucesso!"),
            @ApiResponse(responseCode = "404", description = "O clube não existir!"),
            @ApiResponse(responseCode = "500", description = "Serviço indisponível!"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Partida> buscarPartidaPorId(@PathVariable Long id) {
        Partida partida = partidaService.buscarPartidaPorId(id);
        return new ResponseEntity<>(partida, HttpStatus.OK);
    }

    @Operation(summary = "Listando as Partidas", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listando as Partidas com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Serviço indisponível!"),
    })
    @GetMapping
    public ResponseEntity<Page<Partida>> listarPartidas(
            @RequestParam(required = false) Long clubeId,
            @RequestParam(required = false) Long estadioId,
            Pageable pageable) {
        Page<Partida> partidas = partidaService.listarPartidas(clubeId, estadioId, pageable);
        return new ResponseEntity<>(partidas, HttpStatus.OK);
    }

    @Operation(summary = "Retrospecto geral de um clube", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrospecto geral de um clube com sucesso!"),
            @ApiResponse(responseCode = "404", description = "O clube não existir!"),
            @ApiResponse(responseCode = "500", description = "Serviço indisponível!"),
    })
    @GetMapping("/retrospecto/{clubeId}")
    public ResponseEntity<?> retrospectoGeral(@PathVariable Long clubeId) {
        Clube clube = clubeRepository.findById(clubeId).orElse(null);
        if (clube == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Map<String, Integer> retrospecto = partidaService.calcularRetrospectoGeral(clube);
        return new ResponseEntity<>(retrospecto, HttpStatus.OK);
    }

    @Operation(summary = "Retrospecto de um clube contra seus adversários", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrospecto de um clube contra seus adversários com sucesso!"),
            @ApiResponse(responseCode = "404", description = "O clube não existir!"),
            @ApiResponse(responseCode = "500", description = "Serviço indisponível!"),
    })
    @GetMapping("/retrospecto/adversarios/{clubeId}")
    public ResponseEntity<?> retrospectoContraAdversarios(@PathVariable Long clubeId) {
        Clube clube = clubeRepository.findById(clubeId).orElse(null);
        if (clube == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Map<Clube, Map<String, Integer>> retrospecto = partidaService.calcularRetrospectoContraAdversarios(clube);
        return new ResponseEntity<>(retrospecto, HttpStatus.OK);
    }

    @Operation(summary = "Confrontos diretos", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Confrontos diretos com sucesso!"),
            @ApiResponse(responseCode = "404", description = "O clube não existir!"),
            @ApiResponse(responseCode = "500", description = "Serviço indisponível!"),
    })
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

    @Operation(summary = "Ranking", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ranking com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Serviço indisponível!"),
    })
    @GetMapping("/ranking")
    public ResponseEntity<?> ranking(@RequestParam String tipo) {
        try {
            List<Map<String, Object>> ranking = partidaService.calcularRanking(tipo);
            return new ResponseEntity<>(ranking, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @Operation(summary = "Listando por filtros", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ranking com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Serviço indisponível!"),
    })
    @GetMapping("/listar/filtros")
    public ResponseEntity<?> listarPartidasComFiltros(
            @RequestParam(required = false) Long clubeId,
            @RequestParam(required = false) Boolean goleadas,
            @RequestParam(required = false) Boolean mandante,
            @RequestParam(required = false) Boolean visitante) {
        List<Partida> partidas = partidaService.listarPartidasComFiltros(clubeId, goleadas, mandante, visitante);
        return new ResponseEntity<>(partidas, HttpStatus.OK);
    }

    @Operation(summary = "Ranking por filtros", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ranking com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Serviço indisponível!"),
    })
    @GetMapping("/ranking/filtros")
    public ResponseEntity<?> ranking(
            @RequestParam String tipo,
            @RequestParam(required = false) Boolean mandante,
            @RequestParam(required = false) Boolean visitante) {
        List<Map<String, Object>> ranking = partidaService.calcularRankingComFiltros(tipo, mandante, visitante);
        return new ResponseEntity<>(ranking, HttpStatus.OK);
    }

}

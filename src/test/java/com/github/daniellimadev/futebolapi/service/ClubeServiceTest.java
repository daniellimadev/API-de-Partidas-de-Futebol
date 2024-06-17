package com.github.daniellimadev.futebolapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.github.daniellimadev.futebolapi.model.Clube;
import com.github.daniellimadev.futebolapi.repository.ClubeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ClubeServiceTest {

    @Mock
    private ClubeRepository clubeRepository;

    @InjectMocks
    private ClubeService clubeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCadastrarClube_Success() {
        Clube clube = new Clube();
        clube.setNome("Clube Teste");
        clube.setEstado("SP");
        clube.setDataCriacao(LocalDate.of(2020, 1, 1));
        clube.setAtivo(true);

        when(clubeRepository.findByNomeAndEstado(clube.getNome(), clube.getEstado())).thenReturn(Optional.empty());
        when(clubeRepository.save(clube)).thenReturn(clube);

        Clube result = clubeService.cadastrarClube(clube);

        assertNotNull(result);
        assertEquals("Clube Teste", result.getNome());
    }

    @Test
    public void testCadastrarClube_ClubeJaExiste() {
        Clube clube = new Clube();
        clube.setNome("Clube Teste");
        clube.setEstado("SP");
        clube.setDataCriacao(LocalDate.of(2020, 1, 1));
        clube.setAtivo(true);

        when(clubeRepository.findByNomeAndEstado(clube.getNome(), clube.getEstado())).thenReturn(Optional.of(clube));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clubeService.cadastrarClube(clube);
        });

        assertEquals("Clube já existe.", exception.getMessage());
    }

    @Test
    public void testCadastrarClube_InvalidNome() {
        Clube clube = new Clube();
        clube.setNome("A");
        clube.setEstado("SP");
        clube.setDataCriacao(LocalDate.of(2020, 1, 1));
        clube.setAtivo(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            clubeService.cadastrarClube(clube);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    public void testEditarClube_Success() {
        Clube clubeAtualizado = new Clube();
        clubeAtualizado.setNome("Clube Atualizado");
        clubeAtualizado.setEstado("RJ");
        clubeAtualizado.setDataCriacao(LocalDate.of(2021, 1, 1));
        clubeAtualizado.setAtivo(true);

        Clube clubeExistente = new Clube();
        clubeExistente.setNome("Clube Existente");
        clubeExistente.setEstado("SP");
        clubeExistente.setDataCriacao(LocalDate.of(2020, 1, 1));
        clubeExistente.setAtivo(true);

        when(clubeRepository.findById(1L)).thenReturn(Optional.of(clubeExistente));
        when(clubeRepository.save(any(Clube.class))).thenReturn(clubeAtualizado);

        Clube result = clubeService.editarClube(1L, clubeAtualizado);

        assertNotNull(result);
        assertEquals("Clube Atualizado", result.getNome());
    }

    @Test
    public void testEditarClube_NotFound() {
        Clube clubeAtualizado = new Clube();
        clubeAtualizado.setNome("Clube Atualizado");
        clubeAtualizado.setEstado("RJ");
        clubeAtualizado.setDataCriacao(LocalDate.of(2021, 1, 1));
        clubeAtualizado.setAtivo(true);

        when(clubeRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clubeService.editarClube(1L, clubeAtualizado);
        });

        assertEquals("Clube não encontrado.", exception.getMessage());
    }

    @Test
    public void testInativarClube_Success() {
        Clube clube = new Clube();
        clube.setNome("Clube Teste");
        clube.setEstado("SP");
        clube.setDataCriacao(LocalDate.of(2020, 1, 1));
        clube.setAtivo(true);

        when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube));

        clubeService.inativarClube(1L);

        verify(clubeRepository, times(1)).save(clube);
        assertFalse(clube.isAtivo());
    }

    @Test
    public void testInativarClube_NotFound() {
        when(clubeRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clubeService.inativarClube(1L);
        });

        assertEquals("Clube não encontrado.", exception.getMessage());
    }

    @Test
    public void testBuscarClubePorId_Success() {
        Clube clube = new Clube();
        clube.setNome("Clube Teste");
        clube.setEstado("SP");
        clube.setDataCriacao(LocalDate.of(2020, 1, 1));
        clube.setAtivo(true);

        when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube));

        Clube result = clubeService.buscarClubePorId(1L);

        assertNotNull(result);
        assertEquals("Clube Teste", result.getNome());
    }

    @Test
    public void testBuscarClubePorId_NotFound() {
        when(clubeRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clubeService.buscarClubePorId(1L);
        });

        assertEquals("Clube não encontrado.", exception.getMessage());
    }

    @Test
    public void testListarClubes() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Clube> clubes = new PageImpl<>(List.of(new Clube()));

        when(clubeRepository.findAll(pageable)).thenReturn(clubes);

        Page<Clube> result = clubeService.listarClubes(null, null, null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testListarClubesByNome() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Clube> clubes = new PageImpl<>(List.of(new Clube()));

        when(clubeRepository.findByNomeContaining("Clube Teste", pageable)).thenReturn(clubes);

        Page<Clube> result = clubeService.listarClubes("Clube Teste", null, null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testListarClubesByEstado() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Clube> clubes = new PageImpl<>(List.of(new Clube()));

        when(clubeRepository.findByEstado("SP", pageable)).thenReturn(clubes);

        Page<Clube> result = clubeService.listarClubes(null, "SP", null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testListarClubesByAtivo() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Clube> clubes = new PageImpl<>(List.of(new Clube()));

        when(clubeRepository.findByAtivo(true, pageable)).thenReturn(clubes);

        Page<Clube> result = clubeService.listarClubes(null, null, true, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
}

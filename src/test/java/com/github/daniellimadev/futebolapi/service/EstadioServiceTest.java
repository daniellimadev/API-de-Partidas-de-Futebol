package com.github.daniellimadev.futebolapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.github.daniellimadev.futebolapi.model.Estadio;
import com.github.daniellimadev.futebolapi.repository.EstadioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class EstadioServiceTest {

    @Mock
    private EstadioRepository estadioRepository;

    @InjectMocks
    private EstadioService estadioService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCadastrarEstadio_Success() {
        Estadio estadio = new Estadio();
        estadio.setNome("Estadio Teste");

        when(estadioRepository.findByNome(estadio.getNome())).thenReturn(Optional.empty());
        when(estadioRepository.save(estadio)).thenReturn(estadio);

        Estadio result = estadioService.cadastrarEstadio(estadio);

        assertNotNull(result);
        assertEquals("Estadio Teste", result.getNome());
    }

    @Test
    public void testCadastrarEstadio_EstadioJaExiste() {
        Estadio estadio = new Estadio();
        estadio.setNome("Estadio Teste");

        when(estadioRepository.findByNome(estadio.getNome())).thenReturn(Optional.of(estadio));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            estadioService.cadastrarEstadio(estadio);
        });

        assertEquals("Estádio já existe.", exception.getMessage());
    }

    @Test
    public void testEditarEstadio_Success() {
        Estadio estadioAtualizado = new Estadio();
        estadioAtualizado.setNome("Estadio Atualizado");

        Estadio estadioExistente = new Estadio();
        estadioExistente.setNome("Estadio Existente");

        when(estadioRepository.findById(1L)).thenReturn(Optional.of(estadioExistente));
        when(estadioRepository.findByNome(estadioAtualizado.getNome())).thenReturn(Optional.empty());
        when(estadioRepository.save(any(Estadio.class))).thenReturn(estadioAtualizado);

        Estadio result = estadioService.editarEstadio(1L, estadioAtualizado);

        assertNotNull(result);
        assertEquals("Estadio Atualizado", result.getNome());
    }

    @Test
    public void testEditarEstadio_NotFound() {
        Estadio estadioAtualizado = new Estadio();
        estadioAtualizado.setNome("Estadio Atualizado");

        when(estadioRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            estadioService.editarEstadio(1L, estadioAtualizado);
        });

        assertEquals("Estádio não encontrado.", exception.getMessage());
    }

    @Test
    public void testBuscarEstadioPorId_Success() {
        Estadio estadio = new Estadio();
        estadio.setNome("Estadio Teste");

        when(estadioRepository.findById(1L)).thenReturn(Optional.of(estadio));

        Estadio result = estadioService.buscarEstadioPorId(1L);

        assertNotNull(result);
        assertEquals("Estadio Teste", result.getNome());
    }

    @Test
    public void testBuscarEstadioPorId_NotFound() {
        when(estadioRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            estadioService.buscarEstadioPorId(1L);
        });

        assertEquals("Estádio não encontrado.", exception.getMessage());
    }

    @Test
    public void testListarEstadios() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Estadio> estadios = new PageImpl<>(List.of(new Estadio()));

        when(estadioRepository.findAll(pageable)).thenReturn(estadios);

        Page<Estadio> result = estadioService.listarEstadios(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
}


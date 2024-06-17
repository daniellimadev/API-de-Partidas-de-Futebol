package com.github.daniellimadev.futebolapi.service;

import com.github.daniellimadev.futebolapi.model.Clube;
import com.github.daniellimadev.futebolapi.model.Estadio;
import com.github.daniellimadev.futebolapi.model.Partida;
import com.github.daniellimadev.futebolapi.repository.ClubeRepository;
import com.github.daniellimadev.futebolapi.repository.EstadioRepository;
import com.github.daniellimadev.futebolapi.repository.PartidaRepository;
import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PartidaServiceTest {

    @InjectMocks
    private PartidaService partidaService;

    @Mock
    private PartidaRepository partidaRepository;

    @Mock
    private ClubeRepository clubeRepository;

    @Mock
    private EstadioRepository estadioRepository;

    private Partida partida;
    private Partida partidaExistente;
    private Clube clubeCasa;
    private Clube clubeVisitante;
    private Estadio estadio;

    private Pageable pageable;
    private Page<Partida> partidas;

    private Clube clube1;
    private Clube clube2;
    private Clube clube3;

    private Clube clube;
    private Partida partida1;
    private Partida partida2;
    private Partida partida3;

    private Clube adversario1;
    private Clube adversario2;
    private Partida partida4;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        clubeCasa = new Clube();
        clubeCasa.setId(1L);
        clubeCasa.setNome("Clube Casa");
        clubeCasa.setAtivo(true);

        clubeVisitante = new Clube();
        clubeVisitante.setId(2L);
        clubeVisitante.setNome("Clube Visitante");
        clubeVisitante.setAtivo(true);

        estadio = new Estadio();
        estadio.setId(1L);
        estadio.setNome("Estádio");

        partida = new Partida();
        partida.setClubeCasa(clubeCasa);
        partida.setClubeVisitante(clubeVisitante);
        partida.setEstadio(estadio);
        partida.setDataHora(LocalDateTime.now().plusDays(1));

        partidaExistente = new Partida();
        partidaExistente.setId(1L);
        partidaExistente.setClubeCasa(clubeCasa);
        partidaExistente.setClubeVisitante(clubeVisitante);
        partidaExistente.setEstadio(estadio);
        partidaExistente.setDataHora(LocalDateTime.now().plusDays(1));

        pageable = PageRequest.of(0, 10);
        partidas = new PageImpl<>(List.of(new Partida()));

        clube1 = new Clube();
        clube1.setNome("Clube 1");

        clube2 = new Clube();
        clube2.setNome("Clube 2");

        clube3 = new Clube();
        clube3.setNome("Clube 3");

        partida1 = new Partida();
        partida1.setId(1L);

        partida2 = new Partida();
        partida2.setId(2L);

        partida3 = new Partida();
        partida3.setId(3L);


        clube = new Clube();
        clube.setId(1L);
        clube.setNome("Clube 1");

        Clube outroClube = new Clube();
        outroClube.setId(2L);
        outroClube.setNome("Clube 2");

        partida1 = new Partida();
        partida1.setClubeCasa(clube);
        partida1.setClubeVisitante(outroClube);
        partida1.setGolsCasa(3);
        partida1.setGolsVisitante(1);

        partida2 = new Partida();
        partida2.setClubeCasa(outroClube);
        partida2.setClubeVisitante(clube);
        partida2.setGolsCasa(2);
        partida2.setGolsVisitante(2);

        partida3 = new Partida();
        partida3.setClubeCasa(clube);
        partida3.setClubeVisitante(outroClube);
        partida3.setGolsCasa(0);
        partida3.setGolsVisitante(1);


        adversario1 = new Clube();
        adversario1.setId(2L);
        adversario1.setNome("Adversario 1");

        adversario2 = new Clube();
        adversario2.setId(3L);
        adversario2.setNome("Adversario 2");

        partida1 = new Partida();
        partida1.setClubeCasa(clube);
        partida1.setClubeVisitante(adversario1);
        partida1.setGolsCasa(3);
        partida1.setGolsVisitante(1);

        partida2 = new Partida();
        partida2.setClubeCasa(adversario1);
        partida2.setClubeVisitante(clube);
        partida2.setGolsCasa(2);
        partida2.setGolsVisitante(2);

        partida3 = new Partida();
        partida3.setClubeCasa(clube);
        partida3.setClubeVisitante(adversario2);
        partida3.setGolsCasa(0);
        partida3.setGolsVisitante(1);

        partida4 = new Partida();
        partida4.setClubeCasa(adversario2);
        partida4.setClubeVisitante(clube);
        partida4.setGolsCasa(1);
        partida4.setGolsVisitante(2);

    }


    @Test
    public void testCadastrarPartidaComSucesso() {
        when(clubeRepository.findById(clubeCasa.getId())).thenReturn(Optional.of(clubeCasa));
        when(clubeRepository.findById(clubeVisitante.getId())).thenReturn(Optional.of(clubeVisitante));
        when(estadioRepository.findById(estadio.getId())).thenReturn(Optional.of(estadio));
        when(partidaRepository.save(any(Partida.class))).thenReturn(partida);

        Partida resultado = partidaService.cadastrarPartida(partida);

        assertEquals(partida, resultado);
    }

    @Test
    public void testCadastrarPartidaComClubeInativo() {
        clubeCasa.setAtivo(false);
        lenient().when(clubeRepository.findById(clubeCasa.getId())).thenReturn(Optional.of(clubeCasa));
        lenient().when(clubeRepository.findById(clubeVisitante.getId())).thenReturn(Optional.of(clubeVisitante));
        lenient().when(estadioRepository.findById(estadio.getId())).thenReturn(Optional.of(estadio));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> partidaService.cadastrarPartida(partida));
        assertEquals("Clube inativo.", exception.getMessage());
    }

    @Test
    public void testCadastrarPartidaComEstadioOcupado() {
        when(clubeRepository.findById(clubeCasa.getId())).thenReturn(Optional.of(clubeCasa));
        when(clubeRepository.findById(clubeVisitante.getId())).thenReturn(Optional.of(clubeVisitante));
        when(estadioRepository.findById(estadio.getId())).thenReturn(Optional.of(estadio));
        when(partidaRepository.findPartidasByEstadioAndDataHora(estadio, partida.getDataHora())).thenReturn(List.of(partida));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> partidaService.cadastrarPartida(partida));
        assertEquals("O estádio já possui uma partida cadastrada para o mesmo dia.", exception.getMessage());
    }

    @Test
    public void testCadastrarPartidaComMesmoClube() {
        partida.setClubeVisitante(clubeCasa);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> partidaService.cadastrarPartida(partida));
        assertEquals("Os clubes da casa e visitante devem ser diferentes.", exception.getMessage());
    }

    @Test
    public void testBuscarPartidaPorId_Success() {
        Partida partida = new Partida();
        partida.setClubeCasa(new Clube(1L, "Clube Casa", "SP", true, LocalDate.now()));
        partida.setClubeVisitante(new Clube(2L, "Clube Visitante", "RJ", true, LocalDate.now()));
        partida.setEstadio(new Estadio(1L, "Estadio Teste"));
        partida.setDataHora(LocalDateTime.now().plusDays(1));

        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partida));

        Partida result = partidaService.buscarPartidaPorId(1L);

        assertNotNull(result);
        assertEquals(partida.getClubeCasa().getNome(), result.getClubeCasa().getNome());
    }

    @Test
    public void testEditarPartidaNaoEncontrada() {
        when(partidaRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            @Valid Partida partidaAtualizada = new Partida();
            partidaService.editarPartida(1L, partidaAtualizada);
        });

        assertEquals("Partida não encontrada.", exception.getMessage());
    }

    @Test
    public void testEditarPartida() {
        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partidaExistente));
        when(clubeRepository.findById(clubeCasa.getId())).thenReturn(Optional.of(clubeCasa));
        when(clubeRepository.findById(clubeVisitante.getId())).thenReturn(Optional.of(clubeVisitante));
        when(estadioRepository.findById(estadio.getId())).thenReturn(Optional.of(estadio));
        when(partidaRepository.save(any(Partida.class))).thenReturn(partidaExistente);

        @Valid Partida partidaAtualizada = new Partida();
        partidaAtualizada.setClubeCasa(clubeCasa);
        partidaAtualizada.setClubeVisitante(clubeVisitante);
        partidaAtualizada.setGolsCasa(2);
        partidaAtualizada.setGolsVisitante(1);
        partidaAtualizada.setEstadio(estadio);
        partidaAtualizada.setDataHora(LocalDateTime.now().plusDays(2));

        Partida resultado = partidaService.editarPartida(1L, partidaAtualizada);

        assertNotNull(resultado);
        assertEquals(partidaAtualizada.getClubeCasa().getNome(), resultado.getClubeCasa().getNome());
        assertEquals(partidaAtualizada.getClubeVisitante().getNome(), resultado.getClubeVisitante().getNome());
        assertEquals(partidaAtualizada.getGolsCasa(), resultado.getGolsCasa());
        assertEquals(partidaAtualizada.getGolsVisitante(), resultado.getGolsVisitante());
        assertEquals(partidaAtualizada.getEstadio(), resultado.getEstadio());
        assertEquals(partidaAtualizada.getDataHora(), resultado.getDataHora());

        verify(partidaRepository).findById(1L);
        verify(partidaRepository).save(partidaExistente);
    }

    @Test
    public void testRemoverPartida_Success() {
        Partida partida = new Partida();
        partida.setClubeCasa(new Clube(1L, "Clube Casa", "SP", true, LocalDate.now()));
        partida.setClubeVisitante(new Clube(2L, "Clube Visitante", "RJ", true, LocalDate.now()));
        partida.setEstadio(new Estadio(1L, "Estadio Teste"));
        partida.setDataHora(LocalDateTime.now().plusDays(1));

        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partida));

        partidaService.removerPartida(1L);

        verify(partidaRepository, times(1)).delete(partida);
    }

    @Test
    public void testListarPartidas() {
        Long clubeId = null;
        Long estadioId = null;

        when(partidaRepository.findByClubeOrEstadio(null, null, pageable)).thenReturn(partidas);

        Page<Partida> result = partidaService.listarPartidas(clubeId, estadioId, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testCalcularRetrospectoGeral() {
        Clube clube = new Clube();
        clube.setNome("Clube A");

        Partida partida1 = new Partida();
        partida1.setClubeCasa(clube);
        partida1.setClubeVisitante(new Clube());
        partida1.setGolsCasa(2);
        partida1.setGolsVisitante(1);

        Partida partida2 = new Partida();
        partida2.setClubeCasa(new Clube());
        partida2.setClubeVisitante(clube);
        partida2.setGolsCasa(0);
        partida2.setGolsVisitante(0);

        Partida partida3 = new Partida();
        partida3.setClubeCasa(clube);
        partida3.setClubeVisitante(new Clube());
        partida3.setGolsCasa(1);
        partida3.setGolsVisitante(2);

        List<Partida> partidas = Arrays.asList(partida1, partida2, partida3);
        when(partidaRepository.findByClube(clube)).thenReturn(partidas);

        Map<String, Integer> retrospecto = partidaService.calcularRetrospectoGeral(clube);

        assertEquals(1, retrospecto.get("vitorias"));
        assertEquals(1, retrospecto.get("empates"));
        assertEquals(1, retrospecto.get("derrotas"));
        assertEquals(3, retrospecto.get("golsFeitos"));
        assertEquals(3, retrospecto.get("golsSofridos"));
    }

    @Test
    public void testCalcularRankingPorPontos() {
        when(clubeRepository.findAll()).thenReturn(Arrays.asList(clube1, clube2, clube3));

        PartidaService spyService = spy(partidaService);
        doReturn(Map.of("vitorias", 5, "empates", 2, "derrotas", 3, "golsFeitos", 15)).when(spyService).calcularRetrospectoGeral(clube1);
        doReturn(Map.of("vitorias", 3, "empates", 4, "derrotas", 3, "golsFeitos", 12)).when(spyService).calcularRetrospectoGeral(clube2);
        doReturn(Map.of("vitorias", 6, "empates", 1, "derrotas", 3, "golsFeitos", 18)).when(spyService).calcularRetrospectoGeral(clube3);

        List<Map<String, Object>> ranking = spyService.calcularRanking("pontos");

        assertEquals(3, ranking.size());

        assertEquals("Clube 3", ranking.get(0).get("clube"));
        assertEquals(19, ranking.get(0).get("pontos"));

        assertEquals("Clube 1", ranking.get(1).get("clube"));
        assertEquals(17, ranking.get(1).get("pontos"));

        assertEquals("Clube 2", ranking.get(2).get("clube"));
        assertEquals(13, ranking.get(2).get("pontos"));
    }

    @Test
    public void testCalcularRankingPorVitorias() {
        when(clubeRepository.findAll()).thenReturn(Arrays.asList(clube1, clube2, clube3));

        PartidaService spyService = spy(partidaService);
        doReturn(Map.of("vitorias", 5, "empates", 2, "derrotas", 3, "golsFeitos", 15)).when(spyService).calcularRetrospectoGeral(clube1);
        doReturn(Map.of("vitorias", 3, "empates", 4, "derrotas", 3, "golsFeitos", 12)).when(spyService).calcularRetrospectoGeral(clube2);
        doReturn(Map.of("vitorias", 6, "empates", 1, "derrotas", 3, "golsFeitos", 18)).when(spyService).calcularRetrospectoGeral(clube3);

        List<Map<String, Object>> ranking = spyService.calcularRanking("vitorias");

        assertEquals(3, ranking.size());

        assertEquals("Clube 3", ranking.get(0).get("clube"));
        assertEquals(6, ranking.get(0).get("vitorias"));

        assertEquals("Clube 1", ranking.get(1).get("clube"));
        assertEquals(5, ranking.get(1).get("vitorias"));

        assertEquals("Clube 2", ranking.get(2).get("clube"));
        assertEquals(3, ranking.get(2).get("vitorias"));
    }

    @Test
    public void testCalcularRankingInvalido() {
        when(clubeRepository.findAll()).thenReturn(Arrays.asList(clube1, clube2, clube3));

        PartidaService spyService = spy(partidaService);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            spyService.calcularRanking("invalido");
        });

        assertEquals("Tipo de ranking inválido", exception.getMessage());
    }

    @Test
    public void testListarPartidasComFiltros_ClubeNull() {
        when(clubeRepository.findById(1L)).thenReturn(Optional.empty());
        when(partidaRepository.findAll()).thenReturn(Arrays.asList(partida1, partida2, partida3));

        List<Partida> partidas = partidaService.listarPartidasComFiltros(1L, null, null, null);

        assertEquals(3, partidas.size());
        assertTrue(partidas.contains(partida1));
        assertTrue(partidas.contains(partida2));
        assertTrue(partidas.contains(partida3));
    }

    @Test
    public void testListarPartidasComFiltros_Goleadas() {
        when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube));
        when(partidaRepository.findGoleadasByClube(clube)).thenReturn(Arrays.asList(partida1));

        List<Partida> partidas = partidaService.listarPartidasComFiltros(1L, true, null, null);

        assertEquals(1, partidas.size());
        assertTrue(partidas.contains(partida1));
    }

    @Test
    public void testListarPartidasComFiltros_Mandante() {
        when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube));
        when(partidaRepository.findByClubeAsMandante(clube)).thenReturn(Arrays.asList(partida2));

        List<Partida> partidas = partidaService.listarPartidasComFiltros(1L, null, true, null);

        assertEquals(1, partidas.size());
        assertTrue(partidas.contains(partida2));
    }

    @Test
    public void testListarPartidasComFiltros_Visitante() {
        when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube));
        when(partidaRepository.findByClubeAsVisitante(clube)).thenReturn(Arrays.asList(partida3));

        List<Partida> partidas = partidaService.listarPartidasComFiltros(1L, null, null, true);

        assertEquals(1, partidas.size());
        assertTrue(partidas.contains(partida3));
    }

    @Test
    public void testListarPartidasComFiltros_SemFiltrosEspecificos() {
        when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube));
        when(partidaRepository.findByClube(clube)).thenReturn(Arrays.asList(partida1, partida2));

        List<Partida> partidas = partidaService.listarPartidasComFiltros(1L, null, null, null);

        assertEquals(2, partidas.size());
        assertTrue(partidas.contains(partida1));
        assertTrue(partidas.contains(partida2));
    }

    @Test
    public void testListarPartidasComFiltros_GoleadasSemClube() {
        when(partidaRepository.findAll()).thenReturn(Arrays.asList(partida1, partida2, partida3));
        when(partidaRepository.findGoleadas()).thenReturn(Arrays.asList(partida1, partida3));

        List<Partida> partidas = partidaService.listarPartidasComFiltros(null, true, null, null);

        assertEquals(2, partidas.size());
        assertTrue(partidas.contains(partida1));
        assertTrue(partidas.contains(partida3));
    }



    @Test
    public void testCalcularRetrospectoGera() throws Exception {
        List<Partida> partidas = Arrays.asList(partida1, partida2, partida3);

        Method method = PartidaService.class.getDeclaredMethod("calcularRetrospectoGeral", Clube.class, List.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, Integer> retrospecto = (Map<String, Integer>) method.invoke(partidaService, clube, partidas);

        assertEquals(1, retrospecto.get("vitorias"));
        assertEquals(1, retrospecto.get("empates"));
        assertEquals(1, retrospecto.get("derrotas"));
        assertEquals(5, retrospecto.get("golsFeitos"));
        assertEquals(4, retrospecto.get("golsSofridos"));
    }

    @Test
    public void testCalcularRetrospectoContraAdversarios() {
        List<Partida> partidas = Arrays.asList(partida1, partida2, partida3, partida4);

        when(partidaRepository.findByClube(clube)).thenReturn(partidas);

        Map<Clube, Map<String, Integer>> retrospecto = partidaService.calcularRetrospectoContraAdversarios(clube);

        assertNotNull(retrospecto);
        assertEquals(2, retrospecto.size());

        Map<String, Integer> retrospectoAdversario1 = retrospecto.get(adversario1);
        assertNotNull(retrospectoAdversario1);
        assertEquals(1, retrospectoAdversario1.get("vitorias"));
        assertEquals(1, retrospectoAdversario1.get("empates"));
        assertEquals(0, retrospectoAdversario1.get("derrotas"));
        assertEquals(5, retrospectoAdversario1.get("golsFeitos"));
        assertEquals(3, retrospectoAdversario1.get("golsSofridos"));

        Map<String, Integer> retrospectoAdversario2 = retrospecto.get(adversario2);
        assertNotNull(retrospectoAdversario2);
        assertEquals(1, retrospectoAdversario2.get("vitorias"));
        assertEquals(0, retrospectoAdversario2.get("empates"));
        assertEquals(1, retrospectoAdversario2.get("derrotas"));
        assertEquals(2, retrospectoAdversario2.get("golsFeitos"));
        assertEquals(2, retrospectoAdversario2.get("golsSofridos"));
    }
}

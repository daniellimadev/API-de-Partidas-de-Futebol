package com.github.daniellimadev.futebolapi.service;

import com.github.daniellimadev.futebolapi.model.Clube;
import com.github.daniellimadev.futebolapi.model.Estadio;
import com.github.daniellimadev.futebolapi.model.Partida;
import com.github.daniellimadev.futebolapi.repository.ClubeRepository;
import com.github.daniellimadev.futebolapi.repository.EstadioRepository;
import com.github.daniellimadev.futebolapi.repository.PartidaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
public class PartidaService {

    @Autowired
    private PartidaRepository partidaRepository;

    @Autowired
    private ClubeRepository clubeRepository;

    @Autowired
    private EstadioRepository estadioRepository;

    public Partida cadastrarPartida(@Valid Partida partida) {
        validarPartida(partida);
        return partidaRepository.save(partida);
    }

    public Partida editarPartida(Long id, @Valid Partida partidaAtualizada) {
        Partida partida = partidaRepository.findById(id).orElseThrow(() -> new RuntimeException("Partida não encontrada."));
        validarPartida(partidaAtualizada);

        partida.setClubeCasa(partidaAtualizada.getClubeCasa());
        partida.setClubeVisitante(partidaAtualizada.getClubeVisitante());
        partida.setGolsCasa(partidaAtualizada.getGolsCasa());
        partida.setGolsVisitante(partidaAtualizada.getGolsVisitante());
        partida.setEstadio(partidaAtualizada.getEstadio());
        partida.setDataHora(partidaAtualizada.getDataHora());

        return partidaRepository.save(partida);
    }

    public void removerPartida(Long id) {
        Partida partida = partidaRepository.findById(id).orElseThrow(() -> new RuntimeException("Partida não encontrada."));
        partidaRepository.delete(partida);
    }

    public Partida buscarPartidaPorId(Long id) {
        return partidaRepository.findById(id).orElseThrow(() -> new RuntimeException("Partida não encontrada."));
    }

    public Page<Partida> listarPartidas(Long clubeId, Long estadioId, Pageable pageable) {
        Clube clube = clubeId != null ? clubeRepository.findById(clubeId).orElse(null) : null;
        Estadio estadio = estadioId != null ? estadioRepository.findById(estadioId).orElse(null) : null;
        return partidaRepository.findByClubeOrEstadio(clube, estadio, pageable);
    }

    public Map<String, Integer> calcularRetrospectoGeral(Clube clube) {
        List<Partida> partidas = partidaRepository.findByClube(clube);
        Map<String, Integer> retrospecto = new HashMap<>();
        retrospecto.put("vitorias", 0);
        retrospecto.put("empates", 0);
        retrospecto.put("derrotas", 0);
        retrospecto.put("golsFeitos", 0);
        retrospecto.put("golsSofridos", 0);

        for (Partida partida : partidas) {
            boolean isClubeCasa = partida.getClubeCasa().equals(clube);
            int golsFeitos = isClubeCasa ? partida.getGolsCasa() : partida.getGolsVisitante();
            int golsSofridos = isClubeCasa ? partida.getGolsVisitante() : partida.getGolsCasa();

            retrospecto.put("golsFeitos", retrospecto.get("golsFeitos") + golsFeitos);
            retrospecto.put("golsSofridos", retrospecto.get("golsSofridos") + golsSofridos);

            if (golsFeitos > golsSofridos) {
                retrospecto.put("vitorias", retrospecto.get("vitorias") + 1);
            } else if (golsFeitos < golsSofridos) {
                retrospecto.put("derrotas", retrospecto.get("derrotas") + 1);
            } else {
                retrospecto.put("empates", retrospecto.get("empates") + 1);
            }
        }

        return retrospecto;
    }

    public Map<Clube, Map<String, Integer>> calcularRetrospectoContraAdversarios(Clube clube) {
        List<Partida> partidas = partidaRepository.findByClube(clube);
        Map<Clube, Map<String, Integer>> retrospectoAdversarios = new HashMap<>();

        for (Partida partida : partidas) {
            Clube adversario = partida.getClubeCasa().equals(clube) ? partida.getClubeVisitante() : partida.getClubeCasa();
            retrospectoAdversarios.putIfAbsent(adversario, new HashMap<>(Map.of("vitorias", 0, "empates", 0, "derrotas", 0, "golsFeitos", 0, "golsSofridos", 0)));

            boolean isClubeCasa = partida.getClubeCasa().equals(clube);
            int golsFeitos = isClubeCasa ? partida.getGolsCasa() : partida.getGolsVisitante();
            int golsSofridos = isClubeCasa ? partida.getGolsVisitante() : partida.getGolsCasa();

            retrospectoAdversarios.get(adversario).put("golsFeitos", retrospectoAdversarios.get(adversario).get("golsFeitos") + golsFeitos);
            retrospectoAdversarios.get(adversario).put("golsSofridos", retrospectoAdversarios.get(adversario).get("golsSofridos") + golsSofridos);

            if (golsFeitos > golsSofridos) {
                retrospectoAdversarios.get(adversario).put("vitorias", retrospectoAdversarios.get(adversario).get("vitorias") + 1);
            } else if (golsFeitos < golsSofridos) {
                retrospectoAdversarios.get(adversario).put("derrotas", retrospectoAdversarios.get(adversario).get("derrotas") + 1);
            } else {
                retrospectoAdversarios.get(adversario).put("empates", retrospectoAdversarios.get(adversario).get("empates") + 1);
            }
        }

        return retrospectoAdversarios;
    }

    public Map<String, Object> calcularConfrontoDireto(Clube clube1, Clube clube2) {
        List<Partida> partidas = partidaRepository.findByClubeConfronto(clube1, clube2);
        Map<String, Integer> retrospectoClube1 = new HashMap<>(Map.of("vitorias", 0, "empates", 0, "derrotas", 0, "golsFeitos", 0, "golsSofridos", 0));
        Map<String, Integer> retrospectoClube2 = new HashMap<>(Map.of("vitorias", 0, "empates", 0, "derrotas", 0, "golsFeitos", 0, "golsSofridos", 0));

        for (Partida partida : partidas) {
            boolean isClube1Casa = partida.getClubeCasa().equals(clube1);
            int golsClube1 = isClube1Casa ? partida.getGolsCasa() : partida.getGolsVisitante();
            int golsClube2 = isClube1Casa ? partida.getGolsVisitante() : partida.getGolsCasa();

            retrospectoClube1.put("golsFeitos", retrospectoClube1.get("golsFeitos") + golsClube1);
            retrospectoClube1.put("golsSofridos", retrospectoClube1.get("golsSofridos") + golsClube2);
            retrospectoClube2.put("golsFeitos", retrospectoClube2.get("golsFeitos") + golsClube2);
            retrospectoClube2.put("golsSofridos", retrospectoClube2.get("golsSofridos") + golsClube1);

            if (golsClube1 > golsClube2) {
                retrospectoClube1.put("vitorias", retrospectoClube1.get("vitorias") + 1);
                retrospectoClube2.put("derrotas", retrospectoClube2.get("derrotas") + 1);
            } else if (golsClube1 < golsClube2) {
                retrospectoClube1.put("derrotas", retrospectoClube1.get("derrotas") + 1);
                retrospectoClube2.put("vitorias", retrospectoClube2.get("vitorias") + 1);
            } else {
                retrospectoClube1.put("empates", retrospectoClube1.get("empates") + 1);
                retrospectoClube2.put("empates", retrospectoClube2.get("empates") + 1);
            }
        }

        return Map.of("partidas", partidas, "retrospectoClube1", retrospectoClube1, "retrospectoClube2", retrospectoClube2);
    }

    public List<Map<String, Object>> calcularRanking(String tipo) {
        List<Clube> clubes = clubeRepository.findAll();
        List<Map<String, Object>> ranking = new ArrayList<>();

        for (Clube clube : clubes) {
            Map<String, Integer> retrospecto = calcularRetrospectoGeral(clube);
            int pontos = retrospecto.get("vitorias") * 3 + retrospecto.get("empates");
            ranking.add(Map.of(
                    "clube", clube.getNome(),
                    "jogos", retrospecto.get("vitorias") + retrospecto.get("empates") + retrospecto.get("derrotas"),
                    "vitorias", retrospecto.get("vitorias"),
                    "gols", retrospecto.get("golsFeitos"),
                    "pontos", pontos
            ));
        }

        switch (tipo) {
            case "jogos":
                return ranking.stream().filter(r -> (int) r.get("jogos") > 0)
                        .sorted(Comparator.comparingInt(r -> -(int) r.get("jogos")))
                        .collect(Collectors.toList());
            case "vitorias":
                return ranking.stream().filter(r -> (int) r.get("vitorias") > 0)
                        .sorted(Comparator.comparingInt(r -> -(int) r.get("vitorias")))
                        .collect(Collectors.toList());
            case "gols":
                return ranking.stream().filter(r -> (int) r.get("gols") > 0)
                        .sorted(Comparator.comparingInt(r -> -(int) r.get("gols")))
                        .collect(Collectors.toList());
            case "pontos":
                return ranking.stream().filter(r -> (int) r.get("pontos") > 0)
                        .sorted(Comparator.comparingInt(r -> -(int) r.get("pontos")))
                        .collect(Collectors.toList());
            default:
                throw new IllegalArgumentException("Tipo de ranking inválido");
        }
    }


    public List<Partida> listarPartidasComFiltros(Long clubeId, Boolean goleadas, Boolean mandante, Boolean visitante) {
        Clube clube = clubeId != null ? clubeRepository.findById(clubeId).orElse(null) : null;
        List<Partida> partidas;

        if (clube == null) {
            partidas = partidaRepository.findAll();
        } else if (Boolean.TRUE.equals(goleadas)) {
            partidas = partidaRepository.findGoleadasByClube(clube);
        } else if (Boolean.TRUE.equals(mandante)) {
            partidas = partidaRepository.findByClubeAsMandante(clube);
        } else if (Boolean.TRUE.equals(visitante)) {
            partidas = partidaRepository.findByClubeAsVisitante(clube);
        } else {
            partidas = partidaRepository.findByClube(clube);
        }

        if (Boolean.TRUE.equals(goleadas) && clube == null) {
            partidas = partidaRepository.findGoleadas();
        }

        return partidas;
    }

    public List<Map<String, Object>> calcularRankingComFiltros(String tipo, Boolean mandante, Boolean visitante) {
        List<Clube> clubes = clubeRepository.findAll();
        List<Map<String, Object>> ranking = new ArrayList<>();

        for (Clube clube : clubes) {
            List<Partida> partidas = new ArrayList<>();
            if (Boolean.TRUE.equals(mandante)) {
                partidas.addAll(partidaRepository.findByClubeAsMandante(clube));
            }
            if (Boolean.TRUE.equals(visitante)) {
                partidas.addAll(partidaRepository.findByClubeAsVisitante(clube));
            }
            if (!mandante && !visitante) {
                partidas.addAll(partidaRepository.findByClube(clube));
            }

            Map<String, Integer> retrospecto = calcularRetrospectoGeral(clube, partidas);
            int pontos = retrospecto.get("vitorias") * 3 + retrospecto.get("empates");
            ranking.add(Map.of("clube", clube, "jogos", retrospecto.get("vitorias") + retrospecto.get("empates") + retrospecto.get("derrotas"),
                    "vitorias", retrospecto.get("vitorias"), "gols", retrospecto.get("golsFeitos"), "pontos", pontos));
        }

        switch (tipo) {
            case "jogos":
                return ranking.stream().filter(r -> (int) r.get("jogos") > 0).sorted(Comparator.comparingInt(r -> -(int) r.get("jogos"))).collect(Collectors.toList());
            case "vitorias":
                return ranking.stream().filter(r -> (int) r.get("vitorias") > 0).sorted(Comparator.comparingInt(r -> -(int) r.get("vitorias"))).collect(Collectors.toList());
            case "gols":
                return ranking.stream().filter(r -> (int) r.get("gols") > 0).sorted(Comparator.comparingInt(r -> -(int) r.get("gols"))).collect(Collectors.toList());
            case "pontos":
                return ranking.stream().filter(r -> (int) r.get("pontos") > 0).sorted(Comparator.comparingInt(r -> -(int) r.get("pontos"))).collect(Collectors.toList());
            default:
                throw new IllegalArgumentException("Tipo de ranking inválido");
        }
    }

    private Map<String, Integer> calcularRetrospectoGeral(Clube clube, List<Partida> partidas) {
        Map<String, Integer> retrospecto = new HashMap<>();
        retrospecto.put("vitorias", 0);
        retrospecto.put("empates", 0);
        retrospecto.put("derrotas", 0);
        retrospecto.put("golsFeitos", 0);
        retrospecto.put("golsSofridos", 0);

        for (Partida partida : partidas) {
            boolean isClubeCasa = partida.getClubeCasa().equals(clube);
            int golsFeitos = isClubeCasa ? partida.getGolsCasa() : partida.getGolsVisitante();
            int golsSofridos = isClubeCasa ? partida.getGolsVisitante() : partida.getGolsCasa();

            retrospecto.put("golsFeitos", retrospecto.get("golsFeitos") + golsFeitos);
            retrospecto.put("golsSofridos", retrospecto.get("golsSofridos") + golsSofridos);

            if (golsFeitos > golsSofridos) {
                retrospecto.put("vitorias", retrospecto.get("vitorias") + 1);
            } else if (golsFeitos < golsSofridos) {
                retrospecto.put("derrotas", retrospecto.get("derrotas") + 1);
            } else {
                retrospecto.put("empates", retrospecto.get("empates") + 1);
            }
        }

        return retrospecto;
    }

    private void validarPartida(Partida partida) {
        if (partida.getClubeCasa().getId().equals(partida.getClubeVisitante().getId())) {
            throw new RuntimeException("Os clubes da casa e visitante devem ser diferentes.");
        }

        Clube clubeCasa = clubeRepository.findById(partida.getClubeCasa().getId())
                .orElseThrow(() -> new RuntimeException("Clube da casa não encontrado."));
        Clube clubeVisitante = clubeRepository.findById(partida.getClubeVisitante().getId())
                .orElseThrow(() -> new RuntimeException("Clube visitante não encontrado."));

        if (!clubeCasa.isAtivo() || !clubeVisitante.isAtivo()) {
            throw new RuntimeException("Clube inativo.");
        }


        Estadio estadio = estadioRepository.findById(partida.getEstadio().getId())
                .orElseThrow(() -> new RuntimeException("Estádio não encontrado."));

        List<Partida> partidasProximas = partidaRepository.findPartidasWithinTimeFrame(clubeCasa, partida.getDataHora().minusHours(48), partida.getDataHora().plusHours(48));
        partidasProximas.addAll(partidaRepository.findPartidasWithinTimeFrame(clubeVisitante, partida.getDataHora().minusHours(48), partida.getDataHora().plusHours(48)));
        if (!partidasProximas.isEmpty()) {
            throw new RuntimeException("Um dos clubes já possui uma partida marcada com diferença menor do que 48 horas.");
        }

        List<Partida> partidasNoEstadio = partidaRepository.findPartidasByEstadioAndDataHora(estadio, partida.getDataHora());
        if (!partidasNoEstadio.isEmpty()) {
            throw new RuntimeException("O estádio já possui uma partida cadastrada para o mesmo dia.");
        }
    }
}
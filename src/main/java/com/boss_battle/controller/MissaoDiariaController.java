package com.boss_battle.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.boss_battle.dto.MissaoDiariaDTO;
import com.boss_battle.service.missoes.MissaoDiariaService;

@RestController
@RequestMapping("/api/missoes-diarias")
@CrossOrigin("*")
public class MissaoDiariaController {

    private final MissaoDiariaService missaoDiariaService;

    public MissaoDiariaController(MissaoDiariaService missaoDiariaService) {
        this.missaoDiariaService = missaoDiariaService;
    }

    // Mostrar missão do usuário
    @GetMapping("/missoes/{usuarioId}")
    public ResponseEntity<MissaoDiariaDTO> buscarMissao(@PathVariable Long usuarioId) {
        MissaoDiariaDTO dto = missaoDiariaService.buscarMissaoDiaria(usuarioId);
        return ResponseEntity.ok(dto);
    }

    // Resgatar missão de dano
    @PostMapping("/{usuarioId}/resgatar/dano")
    public ResponseEntity<MissaoDiariaDTO> resgatarMissaoDano(@PathVariable Long usuarioId) {
        MissaoDiariaDTO dto = missaoDiariaService.resgatarMissaoDano(usuarioId);
        return ResponseEntity.ok(dto);
    }

    // Resgatar missão de ataques especiais
    @PostMapping("/{usuarioId}/resgatar/ataques")
    public ResponseEntity<MissaoDiariaDTO> resgatarMissaoAtaques(@PathVariable Long usuarioId) {
        MissaoDiariaDTO dto = missaoDiariaService.resgatarMissaoAtaques(usuarioId);
        return ResponseEntity.ok(dto);
    }

    // Atualizar dano
    @PostMapping("/{usuarioId}/atualizar/dano")
    public ResponseEntity<MissaoDiariaDTO> atualizarDano(
            @PathVariable Long usuarioId,
            @RequestParam long valor) {

        missaoDiariaService.atualizarProgressoDano(usuarioId, valor);
        MissaoDiariaDTO dto = missaoDiariaService.buscarMissaoDiaria(usuarioId);
        return ResponseEntity.ok(dto);
    }

    // Atualizar ataques especiais
    @PostMapping("/{usuarioId}/atualizar/ataques")
    public ResponseEntity<MissaoDiariaDTO> atualizarAtaques(
            @PathVariable Long usuarioId,
            @RequestParam int quantidade) {

        missaoDiariaService.atualizarProgressoQuantidade(usuarioId, quantidade);
        MissaoDiariaDTO dto = missaoDiariaService.buscarMissaoDiaria(usuarioId);
        return ResponseEntity.ok(dto);
    }
}
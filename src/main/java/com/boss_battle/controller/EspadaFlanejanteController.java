package com.boss_battle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.boss_battle.service.ativar_equipar.EspadaFlanejanteService;



@RestController
@RequestMapping("/api/espada-flanejante")
public class EspadaFlanejanteController {

    @Autowired
    private EspadaFlanejanteService espadaFlanejanteService;

    /**
     * Equipa (ativa) Espadas Flanejantes
     * ❗ irreversível
     */
    @PostMapping("/ativar")
    public ResponseEntity<?> ativarEspada(
            @RequestParam Long usuarioId,
            @RequestParam int quantidade) {

        try {
            espadaFlanejanteService.ativarEspadaFlanejante(usuarioId, quantidade);
            return ResponseEntity.ok("Espada Flanejante equipada com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

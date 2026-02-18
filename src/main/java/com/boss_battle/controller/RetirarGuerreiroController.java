package com.boss_battle.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.boss_battle.service.ativar_equipar.EquiparGuerreiroService;
import com.boss_battle.service.auto_ataque.RetaguardaService;
@RestController
@RequestMapping("/retirar")
public class RetirarGuerreiroController {

    @Autowired
    private EquiparGuerreiroService equiparGuerreiroService;


    @Autowired
    RetaguardaService  retaguardaService;
    // ==============================================
    // RETIRAR DO ATAQUE
    // POST /retirar/ataque/{usuarioId}
    // ==============================================
    @PostMapping("/ataque/{usuarioId}")
    public ResponseEntity<Map<String, Number>> retirarDoAtaque(
            @PathVariable Long usuarioId) {

        Map<String, Number> resultado =
                equiparGuerreiroService.retirarGuerreiroAtaque(usuarioId);

        if ((int) resultado.get("sucesso") == 0) {
            return ResponseEntity.badRequest().body(resultado);
        }

        return ResponseEntity.ok(resultado);
    }

    // ==============================================
    // RETIRAR DA RETAGUARDA
    // POST /retirar/retaguarda/{usuarioId}
    // ==============================================
    @PostMapping("/retaguarda/{usuarioId}")
    public ResponseEntity<Map<String, Number>> retirarDaRetaguarda(
            @PathVariable Long usuarioId) {

        Map<String, Number> resultado =
                equiparGuerreiroService.retirarGuerreiroRetaguarda(usuarioId);

        if ((int) resultado.get("sucesso") == 0) {
            return ResponseEntity.badRequest().body(resultado);
        }

        return ResponseEntity.ok(resultado);
    }
    
    
  
}

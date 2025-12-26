package com.boss_battle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.boss_battle.dto.ComprarAtaqueEspecialDTO;
import com.boss_battle.service.ComprarAtaqueEspecialService;

@RestController
public class ComprarAtaqueEspecialController {

    @Autowired
    private ComprarAtaqueEspecialService comprarAtaqueEspecialService;

    @PostMapping("/comprar/ataque-especial/{usuarioId}")
    public ResponseEntity<String> comprarAtaqueEspecial(
            @PathVariable Long usuarioId,
            @RequestBody ComprarAtaqueEspecialDTO dto) {

        boolean sucesso = comprarAtaqueEspecialService.comprarAtaqueEspecial(
                usuarioId,
                dto.getQuantidade()
              
        );

        if (sucesso) {
            return ResponseEntity.ok(" com sucesso!");
        } else {
            return ResponseEntity.status(400).body("Saldo insuficiente.");
        }
    }
}

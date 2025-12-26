package com.boss_battle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.boss_battle.dto.ComprarEnergiaDTO;
import com.boss_battle.service.ComprarEnergiaService;

@RestController
public class ComprarEnergiaController {

    @Autowired
    private ComprarEnergiaService comprarEnergiaService;

    @PostMapping("/comprar/energia/{usuarioId}")
    public ResponseEntity<String> comprarEnergia(
            @PathVariable Long usuarioId,
            @RequestBody ComprarEnergiaDTO dto) {


        boolean sucesso = comprarEnergiaService
                .comprarEnergia(usuarioId, dto.getQuantidade());

        if (sucesso) {
            return ResponseEntity.ok("Compra de energia realizada com sucesso!");
        }

        return ResponseEntity
                .status(409)
                .body("Saldo insuficiente.");
    }
}

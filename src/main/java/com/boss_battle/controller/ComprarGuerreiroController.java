package com.boss_battle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.boss_battle.dto.ComprarGuerreiroDTO;
import com.boss_battle.service.aprimoramentos_loja.ComprarGuerreiroService;


@RestController
public class ComprarGuerreiroController {
    
    @Autowired
    private ComprarGuerreiroService comprarGuerreiroService;

    @PostMapping("/comprar/guerreiro/{usuarioId}")
    public ResponseEntity<String> comprarGuerreiro(
            @PathVariable Long usuarioId,
            @RequestBody ComprarGuerreiroDTO dto) {

        boolean sucesso = comprarGuerreiroService.comprarGuerreiro(usuarioId, dto.getQuantidade());

        if (sucesso) {
            return ResponseEntity.ok("Compra realizada com sucesso!");
        } else {
            return ResponseEntity.badRequest().body("Saldo insuficiente.");
        }
    }

}

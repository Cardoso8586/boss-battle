package com.boss_battle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.boss_battle.service.ativar_equipar.PocaoVigorService;


@RestController
@RequestMapping("/api/pocao-vigor")
public class PocaoVigorController {

    @Autowired
    private PocaoVigorService pocaoVigorService;

    /**
     * Ativa poções automáticas de vigor (irreversível)
     */
    @PostMapping("/ativar")
    public ResponseEntity<?> ativarPocao(
            @RequestParam Long usuarioId,
            @RequestParam int quantidade) {

        try {
            pocaoVigorService.ativarPocaoVigor(usuarioId, quantidade);
            return ResponseEntity.ok("Poção de vigor ativada com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    
    
    
}

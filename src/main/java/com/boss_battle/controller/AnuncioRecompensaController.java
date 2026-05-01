package com.boss_battle.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.boss_battle.dto.RecompensaAnuncioDTO;
import com.boss_battle.service.recompensa_anuncio.AnuncioRecompensaService;


@RestController
@RequestMapping("/api/anuncio-recompensa")
public class AnuncioRecompensaController {

    @Autowired
    private AnuncioRecompensaService anuncioRecompensaService;
//--------------------------------------------------------------------------
    @PostMapping("/receber/{usuarioId}")
    public ResponseEntity<?> receberRecompensa(@PathVariable Long usuarioId) {

        try {
            RecompensaAnuncioDTO resposta =
                    anuncioRecompensaService.receberRecompensa(usuarioId);

            return ResponseEntity.ok(resposta);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }//--->receberRecompensa
    
    //-----------------------------------------------------------------
    @GetMapping("/status/{usuarioId}")
    public ResponseEntity<?> statusAnuncio(@PathVariable Long usuarioId) {

        try {
            Map<String, Object> status =
                    anuncioRecompensaService.verificarStatusAnuncio(usuarioId);

            return ResponseEntity.ok(status);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }//--->statusAnuncio
    
    
}//--->AnuncioRecompensaController
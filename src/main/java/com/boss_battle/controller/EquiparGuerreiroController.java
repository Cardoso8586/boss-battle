package com.boss_battle.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.boss_battle.service.ativar_equipar.EquiparGuerreiroService;


@RestController
@RequestMapping("/equipar")
public class EquiparGuerreiroController {

    @Autowired
    private EquiparGuerreiroService equiparGuerreiroService;

    // Endpoint: POST /equipar/guerreiro/{usuarioId}
    @PostMapping("/guerreiro/{usuarioId}")
    public ResponseEntity<Map<String, Number>> equiparGuerreiro(@PathVariable Long usuarioId) {

        Map<String, Number> resultado = equiparGuerreiroService.equiparGuerreiro(usuarioId);

        if ((Integer) resultado.get("sucesso") == 0) {
            return ResponseEntity.badRequest().body(resultado);
        }

        return ResponseEntity.ok(resultado);
    }
    
    
    //===========================

    // Endpoint: POST /equipar/guerreiro/{usuarioId}
    @PostMapping("/retaguarda/{usuarioId}")
    public ResponseEntity<Map<String, Number>> adicionarGuerreirosRetaguarda(@PathVariable Long usuarioId) {

        Map<String, Number> resultado = equiparGuerreiroService.adicionarGuerreirosRetaguarda(usuarioId);

        if ((Integer) resultado.get("sucesso") == 0) {
            return ResponseEntity.badRequest().body(resultado);
        }

        return ResponseEntity.ok(resultado);
    }
  
}

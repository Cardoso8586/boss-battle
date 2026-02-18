package com.boss_battle.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boss_battle.service.auto_ataque.RetaguardaService;



@RestController
@RequestMapping("/retaguarda")
public class RetaguardaController {

    @Autowired
    private RetaguardaService retaguardaService;


    
    @GetMapping("/reparo/{usuarioId}")
    public ResponseEntity<Map<String, Number>> consultarReparo(
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(
            retaguardaService.consultarReparo(usuarioId)
        );
    }
    
}

package com.boss_battle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.boss_battle.dto.ComprarVigorAutoDTO;
import com.boss_battle.service.ComprarVigorAutoService;

@RestController
public class ComprarVigorAutoController {
	
	 @Autowired
	    private ComprarVigorAutoService comprarVigorAutoService;

	    @PostMapping("/comprar/pocao-vigor/{usuarioId}")
	    public ResponseEntity<String> comprarEnergia(
	            @PathVariable Long usuarioId,
	            @RequestBody ComprarVigorAutoDTO dto) {


	        boolean sucesso = comprarVigorAutoService
	                .comprarVigorAuto(usuarioId, dto.getQuantidade());

	        if (sucesso) {
	            return ResponseEntity.ok("Compra de poção de vigor realizada com sucesso!");
	        }

	        return ResponseEntity
	                .status(409)
	                .body("Saldo insuficiente.");
	    }
	
	
	

}//ComprarVigorAutoController

package com.boss_battle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.boss_battle.dto.ComprarEscudoPrimordialDTO;
import com.boss_battle.service.aprimoramentos_loja.ComprarEscudoPrimordialService;

@RestController
public class ComprarEscudoPrimordialController {
	
	
	    @Autowired
	    private ComprarEscudoPrimordialService comprarEscudoPrimordialService;
		
	    @PostMapping("/comprar/escudo/primordial/{usuarioId}")
	    public ResponseEntity<String> comprarEscudoPrimordialService(
	    		  @PathVariable Long usuarioId,
	              @RequestBody ComprarEscudoPrimordialDTO dto) 
	    {
	    	
	    	 boolean sucesso = comprarEscudoPrimordialService.comprarEscudoPrimordial(usuarioId, dto.getQuantidade());
			
	    	   if (sucesso) {
	               return ResponseEntity.ok("Compra realizada com sucesso!");
	           } else {
	               return ResponseEntity.badRequest().body("Saldo insuficiente.");
	           }
	    	 
	    	
	    }
	
	
	

}//--->ComprarEscudoPrimordialController

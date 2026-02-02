package com.boss_battle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.boss_battle.dto.ComprarArcoCelestialDTO;
import com.boss_battle.service.ComprarArcoCelestialService;

@RestController
public class ComprarArcoCelestialController {
	

	   @Autowired
	    private ComprarArcoCelestialService comprarArcoCelestialService;
		
	    @PostMapping("/comprar/arco/celestial/{usuarioId}")
	    public ResponseEntity<String> comprarEspadaFlanejanteService(
	    		  @PathVariable Long usuarioId,
	              @RequestBody ComprarArcoCelestialDTO dto) 
	    {
	    	
	    	 boolean sucesso = comprarArcoCelestialService.comprarArcoCelestial(usuarioId, dto.getQuantidade());
			
	    	   if (sucesso) {
	               return ResponseEntity.ok("Compra realizada com sucesso!");
	           } else {
	               return ResponseEntity.badRequest().body("Saldo insuficiente.");
	           }
	    	 
	    	
	    }
	
	
	
	
	
	
	
	
	
	
	

}//--->ComprarArcoCelestialController

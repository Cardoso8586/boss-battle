package com.boss_battle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.boss_battle.dto.ComprarEspadaFlanejanteDTO;
import com.boss_battle.service.aprimoramentos_loja.ComprarEspadaFlanejanteService;


@RestController
public class ComprarEspadasFlanejantesController {
	
	
    
    @Autowired
    private ComprarEspadaFlanejanteService comprarEspadaFlanejanteService;
	
    @PostMapping("/comprar/espada/flanejante/{usuarioId}")
    public ResponseEntity<String> comprarEspadaFlanejanteService(
    		  @PathVariable Long usuarioId,
              @RequestBody ComprarEspadaFlanejanteDTO dto) 
    {
    	
    	 boolean sucesso = comprarEspadaFlanejanteService.comprarEspadaFlanejante(usuarioId, dto.getQuantidade());
		
    	   if (sucesso) {
               return ResponseEntity.ok("Compra realizada com sucesso!");
           } else {
               return ResponseEntity.badRequest().body("Saldo insuficiente.");
           }
    	 
    	
    }
	

}//--->

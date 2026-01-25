package com.boss_battle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.boss_battle.service.MachadoDilaceradorService;

@RestController


@RequestMapping("/api/machado-dilacerador")
public class MachadoDilaceradorController {

	
	@Autowired
	MachadoDilaceradorService machadoDilaceradorService;
	/**
     * Equipar (ativar) machado dilacerarador
     * ❗ irreversível
     */

	@PostMapping("/ativar")
	public ResponseEntity<?> ativarMachadoDilacerador(
	        @RequestParam Long usuarioId,
	        @RequestParam int quantidade) {
	    try {
	        machadoDilaceradorService.ativarMachadoDilacerador(usuarioId, quantidade);
	        return ResponseEntity.ok("Machado Dilacerador equipado com sucesso");
	    } catch (RuntimeException e) {
	        return ResponseEntity.badRequest().body(e.getMessage());
	    }
	}

	
	
	
	
	
	
	
	
	
}//--->MachadoDilaceradorController

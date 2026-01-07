package com.boss_battle.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ComprarEspadaFlanejanteService {
	//comprar espada flanejante

	 
	 @Autowired
	    private LojaAprimoramentosService lojaService;

	    @Autowired
	    private UsuarioBossBattleRepository repo;

	    
	
	public boolean comprarEspadaFlanejante(Long usuarioId, int quantidade){
		
		UsuarioBossBattle usuario = repo.findByIdForUpdate(usuarioId)
    	        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
	
		 BigDecimal precoUnitario =
	                BigDecimal.valueOf(lojaService.getPrecoBaseEspadaFlanejante());

	        BigDecimal valorTotal =
	                precoUnitario.multiply(BigDecimal.valueOf(quantidade));

	        if (usuario.getBossCoins().compareTo(valorTotal) < 0) {
	            return false;
	        }

	        // 💰 debita
	        usuario.setBossCoins(usuario.getBossCoins().subtract(valorTotal));

	        // ⚔️ adiciona espadas
	        usuario.setEspadaFlanejante(
	                usuario.getEspadaFlanejante() + quantidade
	        );

	        repo.save(usuario);
	        return true;
	}
	
	

}//--->

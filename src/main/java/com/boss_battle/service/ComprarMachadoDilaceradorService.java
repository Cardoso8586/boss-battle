package com.boss_battle.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ComprarMachadoDilaceradorService {

	

    @Autowired
    private LojaAprimoramentosService lojaService;

    @Autowired
    private UsuarioBossBattleRepository repo;
    
    
    
public boolean comprarMachadoDilacerador(Long usuarioId, int quantidade){
		
		UsuarioBossBattle usuario = repo.findByIdForUpdate(usuarioId)
    	        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
	
		 BigDecimal precoUnitario =
	                BigDecimal.valueOf(usuario.getPrecoMachadoDilacerador());

	        BigDecimal valorTotal =
	                precoUnitario.multiply(BigDecimal.valueOf(quantidade));

	        if (usuario.getBossCoins().compareTo(valorTotal) < 0) {
	            return false;
	        }

	        // 💰 debita
	        usuario.setBossCoins(usuario.getBossCoins().subtract(valorTotal));

	        // ⚔️ adiciona machados dilacerador
	        usuario.setMachadoDilacerador(
	                usuario.getMachadoDilacerador() + quantidade
	        );

	        repo.save(usuario);
	        return true;
	}
	
	
    
  
}//--->ComprarMachadoDilaceradorService

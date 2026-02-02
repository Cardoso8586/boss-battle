package com.boss_battle.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ComprarArcoCelestialService {
	
	
	@Autowired
	LojaAprimoramentosService lojaAprimoramentosService;
	
	@Autowired
    private UsuarioBossBattleRepository repo;

    public boolean comprarArcoCelestial(Long usuarioId, int quantidade) {
    	
    	// 🔒 Busca usuário com lock pessimista
        UsuarioBossBattle usuario = repo.findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    	
        Long precoUnitario = lojaAprimoramentosService.getPRECO_ARCO_CELESTIAL();
        BigDecimal valorTotal = BigDecimal.valueOf(precoUnitario).multiply(BigDecimal.valueOf(quantidade));

        
        // ❌ Saldo insuficiente
        if (usuario.getBossCoins().compareTo(valorTotal) < 0) {
            return false;
        }

        // 💰 Debita saldo
        usuario.setBossCoins(usuario.getBossCoins().subtract(valorTotal));

        // ⚔️ Adiciona Arco celestial
       usuario.setInventarioArco(usuario.getInventarioArco() + quantidade);

        // 💾 Salva e força persistência imediata
        repo.saveAndFlush(usuario);

    	
    	 return true;
    	
    }//--->comprarMachadoDilacerador
	
	
	

}//ComprarArcoCelestial

package com.boss_battle.service.aprimoramentos_loja;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ComprarEscudoPrimordialService {

	 @Autowired
	    private UsuarioBossBattleRepository repo;
		@Autowired
		LojaAprimoramentosService lojaAprimoramentosService;
	
		 public boolean comprarEscudoPrimordial(Long usuarioId, int quantidade) {
			 
			  // 🔒 Busca com lock pessimista
		        UsuarioBossBattle usuario = repo.findByIdForUpdate(usuarioId)
		                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));	
			 
		        
		       //BigDecimal precoUnitario = BigDecimal.valueOf(usuario.getPrecoEscudoPrimordial());
		        BigDecimal precoUnitario = BigDecimal.valueOf( lojaAprimoramentosService.getPRECO_ESCUDO_PRIMORDIAL());
			 
		        BigDecimal valorTotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
			 
		        // ❌ Saldo insuficiente
		        if (usuario.getBossCoins().compareTo(valorTotal) < 0) {
		            return false;
		        }

		        // 💰 Debita saldo
		        usuario.setBossCoins(usuario.getBossCoins().subtract(valorTotal));

		        // ⚔️ Adiciona espadas escudo primordial
		        usuario.setEscudoPrimordial(usuario.getEscudoPrimordial() + quantidade);

		        
		        // 🔁 Recalcula preço (sem salvar usuário ainda)
		        lojaAprimoramentosService.atualizarPrecoEscudoPrimordial(usuario, quantidade);
		        
		        // 💾 Salva e força commit imediato
		        repo.saveAndFlush(usuario);
			 
			 return true;
			 
			 
		 }//--->comprarEscudoPrimordial

	
	
	
}//--->ComprarEscudoPrimordialService

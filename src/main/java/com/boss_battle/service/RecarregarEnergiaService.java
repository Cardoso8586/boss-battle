package com.boss_battle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

import jakarta.transaction.Transactional;

@Service
public class RecarregarEnergiaService {
	
	
	 @Autowired
	    private UsuarioBossBattleRepository repo;
	 @Transactional
	 public UsuarioBossBattle recarregarEnergia(Long usuarioId) {
	     UsuarioBossBattle usuario = repo.findById(usuarioId)
	             .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));
	     
	     usuario.setEnergiaGuerreiros(usuario.getEnergiaGuerreirosPadrao());
	     return repo.save(usuario); 
	 }

	 
}

package com.boss_battle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

@Service
@Transactional
public class UsuarioBossBattleService {

    @Autowired
    private LojaAprimoramentosService lojaService;
    @Autowired
    private RandomRewardService randomRewardService;
    @Autowired
    private UsuarioBossBattleRepository repo;

    private static final long XP_POR_NIVEL = 1000;

    public void adicionarExp(Long usuarioId, long expGanha) {

        UsuarioBossBattle usuario = repo.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // ➕ Adiciona XP
        usuario.setExp(usuario.getExp() + expGanha);

        
        // 🔁 Sobe nível se necessário
        while (usuario.getExp() >= XP_POR_NIVEL) {
            usuario.setExp(usuario.getExp() - XP_POR_NIVEL);
            usuario.setNivel(usuario.getNivel() + 1);

            // Buffs por nível
            usuario.setAtaqueBase(usuario.getAtaqueBase() + 1);
            
            
         // 🎲 gera o próximo preview
            randomRewardService.onLevelUp(usuarioId);
            lojaService.atualizarPrecoLoja(usuario);
           
        }

        repo.save(usuario);
    }
    
    
    
}

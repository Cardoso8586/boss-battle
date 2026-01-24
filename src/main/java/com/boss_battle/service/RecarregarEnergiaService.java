package com.boss_battle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RecarregarEnergiaService {

    @Autowired
    private UsuarioBossBattleRepository repo;

    public UsuarioBossBattle recarregarEnergia(Long usuarioId) {

        // 🔒 Busca com lock pessimista
        UsuarioBossBattle usuario = repo.findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));

        // ⚡ Recarrega energia
        usuario.setEnergiaGuerreiros(usuario.getEnergiaGuerreirosPadrao());

        // 💾 Salva e força commit imediato
        return repo.saveAndFlush(usuario);
    }
}

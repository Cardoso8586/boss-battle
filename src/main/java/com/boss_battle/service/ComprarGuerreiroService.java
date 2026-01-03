package com.boss_battle.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ComprarGuerreiroService {

    @Autowired
    private LojaAprimoramentosService lojaService;

    @Autowired
    private UsuarioBossBattleRepository repo;

    @Transactional
    public boolean comprarGuerreiro(Long usuarioId, int quantidade) {

       // UsuarioBossBattle usuario = repo.findById(usuarioId)
            //.orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    	UsuarioBossBattle usuario = repo.findByIdForUpdate(usuarioId)
    	        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    	
        BigDecimal precoUnitario =
                BigDecimal.valueOf(usuario.getPrecoGuerreiros());

        BigDecimal valorTotal =
                precoUnitario.multiply(BigDecimal.valueOf(quantidade));

        if (usuario.getBossCoins().compareTo(valorTotal) < 0) {
            return false;
        }

        // 💰 debita
        usuario.setBossCoins(usuario.getBossCoins().subtract(valorTotal));

        // ⚔️ soma guerreiros
      //  usuario.setGuerreiros(usuario.getGuerreiros() + quantidade);

        // ⚔️ soma guerreiros
        usuario.setGuerreirosInventario(usuario.getGuerreirosInventario() + quantidade);
        
        // 🔁 recalcula preços (SEM salvar)
        lojaService.atualizarPrecosLoja(usuario, quantidade);

        // ✅ UM ÚNICO SAVE
        repo.save(usuario);

        return true;
    }
}



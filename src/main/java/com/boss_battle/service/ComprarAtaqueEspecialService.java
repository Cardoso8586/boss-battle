package com.boss_battle.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

import jakarta.transaction.Transactional;

@Service
public class ComprarAtaqueEspecialService {

    @Autowired
    private LojaAprimoramentosService lojaService;

    @Autowired
    private UsuarioBossBattleRepository repo;

    /**
     * Compra pontos de ataque especial para o usuário
     */
    
    @Transactional
    public boolean comprarAtaqueEspecial(Long usuarioId, int quantidade) {

      //  UsuarioBossBattle usuario = repo.findById(usuarioId)
               // .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    	UsuarioBossBattle usuario = repo.findByIdForUpdate(usuarioId)
    	        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // ✅ preço unitário vem do USUÁRIO (não do front)
        BigDecimal precoUnitario =
                BigDecimal.valueOf(usuario.getPrecoAtaqueEspecial());

        BigDecimal valorTotal =
                precoUnitario.multiply(BigDecimal.valueOf(quantidade));

        // ❌ saldo insuficiente
        if (usuario.getBossCoins().compareTo(valorTotal) < 0) {
            return false;
        }

        // 💰 debita BossCoins
        usuario.setBossCoins(
                usuario.getBossCoins().subtract(valorTotal)
        );

        // ⚔️ cada unidade concede +5 ataque especial
        long ataqueAtual = usuario.getAtaqueBase();
        long novoAtaque = ataqueAtual + (quantidade * 5L);
        usuario.setAtaqueBase(novoAtaque);

        // 🔁 recalcula preços da PRÓXIMA compra
        lojaService.atualizarPrecosLoja(usuario, quantidade);

        repo.save(usuario);
        return true;
    }
}

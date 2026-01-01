package com.boss_battle.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

@Service
public class ComprarEnergiaService {

    @Autowired
    private LojaAprimoramentosService lojaService;

    @Autowired
    private UsuarioBossBattleRepository repo;

    /**
     * Compra energia para os guerreiros.
     */
    @Transactional
    public boolean comprarEnergia(Long usuarioId, int quantidade) {

       // UsuarioBossBattle usuario = repo.findById(usuarioId)
           // .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    	UsuarioBossBattle usuario = repo.findByIdForUpdate(usuarioId)
    	        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    	
        // ✅ preço unitário vem do usuário
        BigDecimal precoUnitario =
                BigDecimal.valueOf(usuario.getPrecoEnergia());

        BigDecimal valorTotal =
                precoUnitario.multiply(BigDecimal.valueOf(quantidade));

        // ✅ verifica saldo
        if (usuario.getBossCoins().compareTo(valorTotal) < 0) {
            return false;
        }

        // 💰 debita BossCoins
        usuario.setBossCoins(
                usuario.getBossCoins().subtract(valorTotal)
        );

        // ⚡ regra do jogo: cada unidade = +5 energia
        long energiaAtual = usuario.getEnergiaGuerreirosPadrao();
        long energiaComprada = quantidade * 5;

        usuario.setEnergiaGuerreirosPadrao(
                energiaAtual + energiaComprada
        );

        // 🔁 recalcula preços (SEM salvar)
        lojaService.atualizarPrecosLoja(usuario, quantidade);

        // ✅ único save
        repo.save(usuario);

        return true;
    }
}

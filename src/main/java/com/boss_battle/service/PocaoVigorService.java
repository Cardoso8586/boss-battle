package com.boss_battle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PocaoVigorService {

    private static final long LIMITE_ENERGIA = 10;
    private static final int QTD_MINIMA_ATIVAR = 1; // 🔧 balanceável

    @Autowired
    private UsuarioBossBattleRepository usuarioRepository;

    /**
     * Ativa poções automáticas de vigor
     * ❗ irreversível
     */
    

    public void ativarPocaoVigor(Long usuarioId, int quantidade) {

      //  UsuarioBossBattle usuario = usuarioRepository.findById(usuarioId)
         //   .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    	 UsuarioBossBattle usuario = usuarioRepository.findByIdForUpdate(usuarioId)
    		        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        // ❌ quantidade inválida
        if (quantidade < QTD_MINIMA_ATIVAR) {
            throw new RuntimeException(
                "Quantidade mínima para ativar é " + QTD_MINIMA_ATIVAR
            );
        }

        // ❌ estoque insuficiente
        if (usuario.getPocaoVigor() < quantidade) {
            throw new RuntimeException("Poções insuficientes no estoque");
        }

        // 🔥 desconta definitivamente do estoque
        usuario.setPocaoVigor(
            usuario.getPocaoVigor() - quantidade
        );

        // 🔥 adiciona às equipadas
        usuario.setPocaoVigorAtiva(
            usuario.getPocaoVigorAtiva() + quantidade
        );

        usuarioRepository.save(usuario);
    }
    
    //===================================================================================

    public boolean verificarEUsarPocaoSeAtiva(UsuarioBossBattle usuario) {

        Long energia = usuario.getEnergiaGuerreiros();

        if (energia == null || energia > LIMITE_ENERGIA) return false;

        // 🔥 verifica ATIVAS (não estoque)
        if (usuario.getPocaoVigorAtiva() <= 0) return false;

        // 🔥 USA 1 POÇÃO
        usuario.setPocaoVigorAtiva(
            usuario.getPocaoVigorAtiva() - 1
        );

        usuario.setEnergiaGuerreiros(
            usuario.getEnergiaGuerreirosPadrao()
        );

        usuarioRepository.save(usuario);

        return true;
    }




}

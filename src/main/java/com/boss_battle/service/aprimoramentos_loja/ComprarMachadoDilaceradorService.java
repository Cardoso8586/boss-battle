package com.boss_battle.service.aprimoramentos_loja;

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
	LojaAprimoramentosService lojaAprimoramentosService;
	
    @Autowired
    private UsuarioBossBattleRepository repo;

    public boolean comprarMachadoDilacerador(Long usuarioId, int quantidade) {

        // 🔒 Busca usuário com lock pessimista
        UsuarioBossBattle usuario = repo.findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

       // BigDecimal precoUnitario = BigDecimal.valueOf(usuario.getPrecoMachadoDilacerador());
        BigDecimal precoUnitario = BigDecimal.valueOf(lojaAprimoramentosService.getPRECO_MACHADO_DILACERADOR());
        BigDecimal valorTotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));

        // ❌ Saldo insuficiente
        if (usuario.getBossCoins().compareTo(valorTotal) < 0) {
            return false;
        }

        // 💰 Debita saldo
        usuario.setBossCoins(usuario.getBossCoins().subtract(valorTotal));

        // ⚔️ Adiciona machados dilacerador
        usuario.setMachadoDilacerador(usuario.getMachadoDilacerador() + quantidade);

        // 💾 Salva e força persistência imediata
        repo.saveAndFlush(usuario);

        return true;
    }
}


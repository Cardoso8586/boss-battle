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

    public boolean comprarGuerreiro(Long usuarioId, int quantidade) {

        // 🔒 Busca usuário com lock pessimista para evitar race conditions
        UsuarioBossBattle usuario = repo.findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        BigDecimal precoUnitario = BigDecimal.valueOf(usuario.getPrecoGuerreiros());
        BigDecimal valorTotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));

        // ❌ Saldo insuficiente
        if (usuario.getBossCoins().compareTo(valorTotal) < 0) {
            return false;
        }

        // 💰 Debita saldo
        usuario.setBossCoins(usuario.getBossCoins().subtract(valorTotal));

        // ⚔️ Adiciona guerreiros ao inventário
        usuario.setGuerreirosInventario(usuario.getGuerreirosInventario() + quantidade);

        // 🔁 Recalcula preço (sem salvar usuário ainda)
        lojaService.atualizarPrecoGuerreiro(usuario, quantidade);

        // ✅ Salva e força persistência imediata
        repo.saveAndFlush(usuario);

        return true;
    }
}

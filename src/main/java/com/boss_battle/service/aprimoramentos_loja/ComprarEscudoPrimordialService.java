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
    private LojaAprimoramentosService lojaAprimoramentosService;

    public boolean comprarEscudoPrimordial(Long usuarioId, int quantidade) {

        // 🔒 Busca usuário com lock pessimista
        UsuarioBossBattle usuario = repo.findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 🚨 SEGURANÇA:
        // Impede quantidade negativa ou zero.
        // Sem esta validação um invasor poderia enviar:
        // quantidade = -9999
        //
        // Isso faria:
        // saldo.subtract(valorNegativo)
        //
        // Resultado:
        // saldo + valor
        //
        // Ou seja, adicionaria BossCoins ao invés de descontar.
        if (quantidade <= 0) {
            return false;
        }

        // 🚨 SEGURANÇA:
        // Limite máximo por compra.
        if (quantidade > 5) {
            return false;
        }

        // Proteção contra saldo nulo
        if (usuario.getBossCoins() == null) {
            usuario.setBossCoins(BigDecimal.ZERO);
        }

        BigDecimal precoUnitario =
                BigDecimal.valueOf(
                        lojaAprimoramentosService.getPRECO_ESCUDO_PRIMORDIAL()
                );

        BigDecimal valorTotal =
                precoUnitario.multiply(BigDecimal.valueOf(quantidade));

        // ❌ Saldo insuficiente
        if (usuario.getBossCoins().compareTo(valorTotal) < 0) {
            return false;
        }

        // 💰 Debita saldo
        usuario.setBossCoins(
                usuario.getBossCoins().subtract(valorTotal)
        );

        // 🛡️ Adiciona escudos ao inventário
        usuario.setEscudoPrimordial(
                usuario.getEscudoPrimordial() + quantidade
        );

        // 🔁 Atualiza preço da loja
        lojaAprimoramentosService
                .atualizarPrecoEscudoPrimordial(usuario, quantidade);

        // 💾 Salva alterações
        repo.saveAndFlush(usuario);

        return true;
    }

}
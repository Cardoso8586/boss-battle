package com.boss_battle.service.aprimoramentos_loja;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ComprarArcoCelestialService {

    private static final int QUANTIDADE_MAXIMA_POR_COMPRA = 5;

    @Autowired
    LojaAprimoramentosService lojaAprimoramentosService;

    @Autowired
    private UsuarioBossBattleRepository repo;

    public boolean comprarArcoCelestial(Long usuarioId, int quantidade) {

        UsuarioBossBattle usuario = repo.findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 🚨 SEGURANÇA
        if (quantidade <= 0) {
            return false;
        }

        // 🚨 SEGURANÇA
        if (quantidade > QUANTIDADE_MAXIMA_POR_COMPRA) {
            return false;
        }

        if (usuario.getBossCoins() == null) {
            usuario.setBossCoins(BigDecimal.ZERO);
        }

        Long precoUnitario =
                lojaAprimoramentosService.getPRECO_ARCO_CELESTIAL();

        BigDecimal valorTotal =
                BigDecimal.valueOf(precoUnitario)
                        .multiply(BigDecimal.valueOf(quantidade));

        if (usuario.getBossCoins().compareTo(valorTotal) < 0) {
            return false;
        }

        usuario.setBossCoins(
                usuario.getBossCoins().subtract(valorTotal)
        );

        usuario.setInventarioArco(
                usuario.getInventarioArco() + quantidade
        );

        repo.saveAndFlush(usuario);

        return true;
    }
}
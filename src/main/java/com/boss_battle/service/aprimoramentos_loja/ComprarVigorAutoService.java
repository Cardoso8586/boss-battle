package com.boss_battle.service.aprimoramentos_loja;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

@Service
@Transactional
public class ComprarVigorAutoService {

    private static final long QUANTIDADE_MAXIMA_POR_COMPRA = 5L;

    @Autowired
    private UsuarioBossBattleRepository repo;

    @Autowired
    private LojaAprimoramentosService lojaAprimoramentosService;

    @Transactional
    public boolean comprarVigorAuto(Long usuarioId, long quantidade) {

        UsuarioBossBattle usuario = repo.findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (quantidade <= 0) {
            return false;
        }

        if (quantidade > QUANTIDADE_MAXIMA_POR_COMPRA) {
            return false;
        }

        if (usuario.getBossCoins() == null) {
            usuario.setBossCoins(BigDecimal.ZERO);
        }

        BigDecimal precoUnitario =
                BigDecimal.valueOf(lojaAprimoramentosService.getPOCAO_VIGOR());

        BigDecimal valorTotal =
                precoUnitario.multiply(BigDecimal.valueOf(quantidade));

        if (usuario.getBossCoins().compareTo(valorTotal) < 0) {
            return false;
        }

        usuario.setBossCoins(usuario.getBossCoins().subtract(valorTotal));

        usuario.setPocaoVigor(usuario.getPocaoVigor() + quantidade);

        repo.save(usuario);

        return true;
    }
}
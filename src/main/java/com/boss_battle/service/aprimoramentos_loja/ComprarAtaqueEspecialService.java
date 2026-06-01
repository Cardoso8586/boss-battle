package com.boss_battle.service.aprimoramentos_loja;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ComprarAtaqueEspecialService {

    private static final long LIMITE_MAXIMO_ATAQUE = 1000L;
    private static final long ATAQUE_POR_UNIDADE = 5L;

    @Autowired
    private LojaAprimoramentosService lojaService;

    @Autowired
    private UsuarioBossBattleRepository repo;

    public boolean comprarAtaqueEspecial(Long usuarioId, int quantidade) {

        UsuarioBossBattle usuario = repo.findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (quantidade <= 0) {
            return false;
        }

        if (usuario.getBossCoins() == null) {
            usuario.setBossCoins(BigDecimal.ZERO);
        }

        long ataqueAtual = usuario.getAtaqueBase();
        long aumentoAtaque = quantidade * ATAQUE_POR_UNIDADE;
        long novoAtaque = ataqueAtual + aumentoAtaque;

        if (ataqueAtual >= LIMITE_MAXIMO_ATAQUE) {
            return false;
        }

        if (novoAtaque > LIMITE_MAXIMO_ATAQUE) {
            return false;
        }

        BigDecimal precoUnitario =
                BigDecimal.valueOf(usuario.getPrecoAtaqueEspecial());

        BigDecimal valorTotal =
                precoUnitario.multiply(BigDecimal.valueOf(quantidade));

        if (usuario.getBossCoins().compareTo(valorTotal) < 0) {
            return false;
        }

        usuario.setBossCoins(usuario.getBossCoins().subtract(valorTotal));

        usuario.setAtaqueBase(novoAtaque);

        lojaService.atualizarPrecoAtaqueEspecial(usuario, quantidade);

        repo.save(usuario);

        return true;
    }
}

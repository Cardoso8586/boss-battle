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

    @Autowired
    private LojaAprimoramentosService lojaService;

    @Autowired
    private UsuarioBossBattleRepository repo;

    /**
     * Compra pontos de ataque especial para o usuário
     */
    
   
    public boolean comprarAtaqueEspecial(Long usuarioId, int quantidade) {

        UsuarioBossBattle usuario = repo.findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (quantidade <= 0) {
            return false;
        }

        long limiteMaximoAtaque = 1000L;

        long ataqueAtual = usuario.getAtaqueBase();

        long aumentoAtaque = quantidade * 5L;

        long novoAtaque = ataqueAtual + aumentoAtaque;

        if (novoAtaque > limiteMaximoAtaque) {
            return false;
        }

        BigDecimal precoUnitario =
                BigDecimal.valueOf(usuario.getPrecoAtaqueEspecial());

        BigDecimal valorTotal =
                precoUnitario.multiply(BigDecimal.valueOf(quantidade));

        if (usuario.getBossCoins().compareTo(valorTotal) < 0) {
            return false;
        }

        usuario.setBossCoins(
                usuario.getBossCoins().subtract(valorTotal)
        );

        usuario.setAtaqueBase(novoAtaque);

        lojaService.atualizarPrecoAtaqueEspecial(usuario, quantidade);

        repo.save(usuario);

        return true;
    }
}

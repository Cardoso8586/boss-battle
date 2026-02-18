package com.boss_battle.service.aprimoramentos_loja;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ComprarEspadaFlanejanteService {

    @Autowired
    private UsuarioBossBattleRepository repo;
	@Autowired
	LojaAprimoramentosService lojaAprimoramentosService;
    public boolean comprarEspadaFlanejante(Long usuarioId, int quantidade) {

        // 🔒 Busca com lock pessimista
        UsuarioBossBattle usuario = repo.findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

       // BigDecimal precoUnitario = BigDecimal.valueOf(usuario.getPrecoEspadaFlanejante());
        BigDecimal precoUnitario = BigDecimal.valueOf( lojaAprimoramentosService.getPRECO_ESPADA_FLANEJANTE());
       
        BigDecimal valorTotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));

        // ❌ Saldo insuficiente
        if (usuario.getBossCoins().compareTo(valorTotal) < 0) {
            return false;
        }

        // 💰 Debita saldo
        usuario.setBossCoins(usuario.getBossCoins().subtract(valorTotal));

        // ⚔️ Adiciona espadas flanejantes
        usuario.setEspadaFlanejante(usuario.getEspadaFlanejante() + quantidade);

        // 💾 Salva e força commit imediato
        repo.saveAndFlush(usuario);

        return true;
    }
}


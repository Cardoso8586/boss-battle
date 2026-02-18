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

    @Autowired
    private UsuarioBossBattleRepository repo;
	@Autowired
	LojaAprimoramentosService lojaAprimoramentosService;
    /**
     * Compra automática de poção de vigor.
     */
    @Transactional
    public boolean comprarVigorAuto(Long usuarioId, long quantidade) {
       // UsuarioBossBattle usuario = repo.findById(usuarioId)
           // .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    	UsuarioBossBattle usuario = repo.findByIdForUpdate(usuarioId)
    	        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Preço unitário da poção
       // BigDecimal precoUnitario = BigDecimal.valueOf(usuario.getPrecoPocaoVigor());
    	 BigDecimal precoUnitario = BigDecimal.valueOf(lojaAprimoramentosService.getPOCAO_VIGOR());
    	
        BigDecimal valorTotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));

        // Verifica se o usuário tem saldo suficiente
        if (usuario.getBossCoins().compareTo(valorTotal) < 0) {
            return false;
        }

        // Debita BossCoins
        usuario.setBossCoins(usuario.getBossCoins().subtract(valorTotal));

        // Soma poções compradas ao estoque atual
        usuario.setPocaoVigor(usuario.getPocaoVigor() + quantidade);

        // Salva alterações
        repo.save(usuario);

        return true;
    }
}

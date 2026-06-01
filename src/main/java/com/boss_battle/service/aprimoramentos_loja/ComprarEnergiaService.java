package com.boss_battle.service.aprimoramentos_loja;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

@Service
@Transactional
public class ComprarEnergiaService {
	private static final long LIMITE_ENERGIA = 50_000L;
	
	
    @Autowired
    private LojaAprimoramentosService lojaService;

    @Autowired
    private UsuarioBossBattleRepository repo;

    /**
     * Compra energia para os guerreiros.
     */
    @Transactional
    public boolean comprarEnergia(Long usuarioId, int quantidade) {

        UsuarioBossBattle usuario = repo.findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (quantidade <= 0) {
            return false;
        }

        if (usuario.getBossCoins() == null) {
            usuario.setBossCoins(BigDecimal.ZERO);
        }

        long energiaAtual = usuario.getEnergiaGuerreirosPadrao();
        long energiaComprada = quantidade * 50L;
        long novaEnergia = energiaAtual + energiaComprada;

        if (novaEnergia > LIMITE_ENERGIA) {
            return false;
        }

        BigDecimal precoUnitario =
                BigDecimal.valueOf(usuario.getPrecoEnergia());

        BigDecimal valorTotal =
                precoUnitario.multiply(BigDecimal.valueOf(quantidade));

        if (usuario.getBossCoins().compareTo(valorTotal) < 0) {
            return false;
        }

        usuario.setBossCoins(usuario.getBossCoins().subtract(valorTotal));

        usuario.setEnergiaGuerreirosPadrao(novaEnergia);

        lojaService.atualizarPrecoVigor(usuario, quantidade);

        repo.save(usuario);

        return true;
    }
}

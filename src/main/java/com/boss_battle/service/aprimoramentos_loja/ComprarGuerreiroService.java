package com.boss_battle.service.aprimoramentos_loja;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ComprarGuerreiroService {
	 private static final long LIMITE_GUERREIRO = 1_000L;
    @Autowired
    private LojaAprimoramentosService lojaService;

    @Autowired
    private UsuarioBossBattleRepository repo;

    public boolean comprarGuerreiro(Long usuarioId, int quantidade) {

        UsuarioBossBattle usuario = repo.findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (quantidade <= 0) {
            return false;
        }

       

        long quantidadeTotalGuerreiro = quantidadeTotalGuerreiro(usuario);

        if (quantidadeTotalGuerreiro + quantidade > LIMITE_GUERREIRO) {
            return false;
        }

        BigDecimal precoUnitario = BigDecimal.valueOf(usuario.getPrecoGuerreiros());
        BigDecimal valorTotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));

        if (usuario.getBossCoins().compareTo(valorTotal) < 0) {
            return false;
        }

        usuario.setBossCoins(usuario.getBossCoins().subtract(valorTotal));

        usuario.setGuerreirosInventario(
                usuario.getGuerreirosInventario() + quantidade
        );

        lojaService.atualizarPrecoGuerreiro(usuario, quantidade);

        repo.save(usuario);

        return true;
    }
    
    public long quantidadeTotalGuerreiro(UsuarioBossBattle usuario) {
    	
      	 //cria qiantidade total de guerreiro por usuario
          long quantGuerreiros = usuario.getGuerreiros();
          long estoqueGuerreiro = usuario.getGuerreirosInventario();
          long  guerreirosRetaguarda = usuario.getGuerreirosRetaguarda();
          long totalGuerreiro = quantGuerreiros + estoqueGuerreiro + guerreirosRetaguarda;
      	
          return totalGuerreiro;
      }
}

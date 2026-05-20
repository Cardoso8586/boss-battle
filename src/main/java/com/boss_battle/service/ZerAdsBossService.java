package com.boss_battle.service;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.service.missoes.MissaoDiariaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ZerAdsBossService {

    @Autowired
    private UltimoValorRecebidoService ultimoValorRecebidoService;
    
    private final UsuarioBossBattleRepository usuarioRepository;
    private final MissaoDiariaService missaoDiariaService;

    private static final BigDecimal EXCHANGE = new BigDecimal("2000");

    public ZerAdsBossService(
    		UsuarioBossBattleRepository usuarioRepository,
    		MissaoDiariaService missaoDiariaService) 
    {
        this.usuarioRepository = usuarioRepository;
        this.missaoDiariaService = missaoDiariaService;
    }
    @Transactional
    public BigDecimal creditarRecompensa(String username,
                                         BigDecimal amount,
                                         Integer clicks) {

    	
        UsuarioBossBattle usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado: " + username));

        BigDecimal recompensa = amount.multiply(EXCHANGE);

        if (usuario.getBossCoins() == null) {
            usuario.setBossCoins(BigDecimal.ZERO);
        }

        usuario.setBossCoins(
                usuario.getBossCoins().add(recompensa)
        );

        if (usuario.getZeradsClicks() == null) {
            usuario.setZeradsClicks(0);
        }

        usuario.setZeradsClicks(
                usuario.getZeradsClicks() + clicks
        );

        usuario.setUltimoValorRecebido(recompensa);

        ultimoValorRecebidoService
                .setUltimoValorRecebido(usuario, recompensa);
        
        missaoDiariaService.atualizarProgressoPtc(usuario.getId(), 1);
        
        // SALVA NO BANCO
        usuarioRepository.save(usuario);

        return recompensa;
    }
    
 
    
    
}
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

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valor inválido");
        }

        int totalClicks = clicks == null || clicks <= 0 ? 1 : clicks;

        // segurança para evitar abuso
        if (totalClicks > 50) {
            throw new RuntimeException("Quantidade de clicks inválida");
        }

        // como agora pode vir pagamento acumulado, esse limite precisa ser maior
        if (amount.compareTo(new BigDecimal("0.50")) > 0) {
            throw new RuntimeException("Amount inválido");
        }

        BigDecimal recompensa = amount.multiply(EXCHANGE);

        // limite maior porque pode vir lote de vários anúncios
        if (recompensa.compareTo(new BigDecimal("1000")) > 0) {
            throw new RuntimeException("Recompensa inválida");
        }

        if (usuario.getBossCoins() == null) {
            usuario.setBossCoins(BigDecimal.ZERO);
        }

        if (usuario.getZeradsClicks() == null) {
            usuario.setZeradsClicks(0);
        }

        usuario.setBossCoins(usuario.getBossCoins().add(recompensa));

        usuario.setZeradsClicks(usuario.getZeradsClicks() + totalClicks);

        usuario.setUltimoValorRecebido(recompensa);

        ultimoValorRecebidoService.setUltimoValorRecebido(usuario, recompensa);

        missaoDiariaService.atualizarProgressoPtc(usuario.getId(), totalClicks);

        usuarioRepository.save(usuario);

        return recompensa;
    }
   
}
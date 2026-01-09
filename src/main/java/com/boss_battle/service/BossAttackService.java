

package com.boss_battle.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

@Service
@Transactional
public class BossAttackService {

    private final Random random = new Random();

    private static final long COOLDOWN_MIN_MINUTOS = 8;
    private static final long COOLDOWN_MAX_MINUTOS = 15;

    // cooldown sorteado e FIXO até o próximo ataque
    private long cooldownSorteadoMinutos = 8;

    @Autowired
    private UsuarioBossBattleRepository repo;

    // Verifica se o jogador pode atacar
    public boolean podeAtacar(UsuarioBossBattle usuario) {
        if (usuario.getUltimoAtaqueBoss() == null) {
            return true;
        }

        
        return usuario.getUltimoAtaqueBoss()
                .plusMinutes(cooldownSorteadoMinutos)
                .isBefore(LocalDateTime.now());
    }

    // Tempo restante para o próximo ataque
    public long tempoRestanteSegundos(UsuarioBossBattle usuario) {
        if (usuario.getUltimoAtaqueBoss() == null) {
            return 0;
        }

        LocalDateTime liberadoEm =
                usuario.getUltimoAtaqueBoss().plusMinutes(cooldownSorteadoMinutos);

        long segundos =
                Duration.between(LocalDateTime.now(), liberadoEm).getSeconds();

        return Math.max(segundos, 0);
    }

  
    public void registrarAtaque(Long usuarioId) {
        int tentativas = 0;

        while (true) {
            try {
                UsuarioBossBattle usuario = repo.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

                cooldownSorteadoMinutos =
                        COOLDOWN_MIN_MINUTOS +
                        random.nextInt((int) (COOLDOWN_MAX_MINUTOS - COOLDOWN_MIN_MINUTOS + 1));

                usuario.setUltimoAtaqueBoss(LocalDateTime.now());
                repo.save(usuario);
                return;

            } catch (Exception e) {
                if (++tentativas >= 3) {
                    throw e; // falha real
                }
                // espera curta antes de tentar de novo
                try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            }
        }
    }

}


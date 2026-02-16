package com.boss_battle.service;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boss_battle.dto.AtaqueBossResponseDTO;
import com.boss_battle.model.BattleBoss;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

@Service
@Transactional
public class BossAutoAttackService {
	
	private String ultimaMensagemAtaque;
	private LocalDateTime ultimoAtaqueEm;



    private final GlobalBossService globalBossService;
    private final UsuarioBossBattleRepository usuarioRepo;

    public BossAutoAttackService(
            GlobalBossService globalBossService,
            UsuarioBossBattleRepository usuarioRepo
    ) {
        this.globalBossService = globalBossService;
        this.usuarioRepo = usuarioRepo;
    }

    @Scheduled(fixedRate = 3000)
    public void verificarAtaqueBoss() {

        BattleBoss boss = globalBossService.getActiveBoss();

        if (boss == null || !boss.isAlive()) return;

        if (boss.getLastAttackAt() == null) {
            boss.setLastAttackAt(LocalDateTime.now());
            return;
        }

        long segundos = Duration.between(
                boss.getLastAttackAt(),
                LocalDateTime.now()
        ).getSeconds();

        if (segundos >= boss.getAttackIntervalSeconds()) {

            executarAtaque(boss);

            boss.setLastAttackAt(LocalDateTime.now());
        }
    }
    
    public AtaqueBossResponseDTO executarAtaque(BattleBoss boss) {

    	 DecimalFormat df = new DecimalFormat("#,##0");
    	 
        long dano = boss.getAttackPower();

        List<UsuarioBossBattle> usuarios = usuarioRepo.findAll();

        for (UsuarioBossBattle u : usuarios) {

            if (u.getEnergiaGuerreiros() != null && u.getEnergiaGuerreiros() > 0) {

                long novaEnergia = u.getEnergiaGuerreiros() - dano;

                u.setEnergiaGuerreiros(Math.max(0, novaEnergia));
                usuarioRepo.save(u);
            }
        }
        String boosName  = boss.getBossName();
        
        System.out.println( boosName+
                " atacou causando " + df.format(dano) + " de dano!");
        
        
        String mensagem = "<span class='boosName'>" + boosName + "</span>"
                + " atacou causando <span class='dano-valor'>"
                + df.format(dano)
                + "</span> de dano!";

        /*
        String mensagem = boss.getBossName()
                + " atacou causando " + df.format(dano) + " de dano!";
*/
        ultimoAtaqueEm = LocalDateTime.now();
        ultimaMensagemAtaque = mensagem;

        return new AtaqueBossResponseDTO(
                boss.getBossName(),
                dano,
                LocalDateTime.now()
        );

    }

    public AtaqueBossResponseDTO getUltimoAtaque() {

        if (ultimoAtaqueEm == null) return null;

        return new AtaqueBossResponseDTO(
                ultimaMensagemAtaque,
                null,
                ultimoAtaqueEm
        );
    }


    //getters / setters
    public String getUltimaMensagem() {
        return ultimaMensagemAtaque;
    }
    
    public LocalDateTime getUltimoAtaqueEm() {
        return ultimoAtaqueEm;
    }
}//BossAutoAttackService
    /*
    public AtaqueBossResponseDTO executarAtaque(BattleBoss boss) {

        long dano = boss.getAttackPower();

        List<UsuarioBossBattle> usuarios = usuarioRepo.findAll();

        for (UsuarioBossBattle u : usuarios) {

            if (u.getEnergiaGuerreiros() != null && u.getEnergiaGuerreiros() > 0) {

                long novaEnergia = u.getEnergiaGuerreiros() - dano;

                u.setEnergiaGuerreiros(Math.max(0, novaEnergia));
                usuarioRepo.save(u);
            }
        }

        System.out.println("🔥 " + boss.getBossName() +
                " atacou causando " + dano + " de dano!");
        
        String mensagem = "🔥 " + boss.getBossName()
                + " atacou causando " + dano + " de dano!";

        ultimaMensagemAtaque = mensagem;

        return new AtaqueBossResponseDTO(mensagem, dano);
    }

    //getters / setters
    public String getUltimaMensagem() {
        return ultimaMensagemAtaque;
    }
*/



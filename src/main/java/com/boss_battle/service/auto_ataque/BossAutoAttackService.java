package com.boss_battle.service.auto_ataque;

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
import com.boss_battle.service.ativar_equipar.EscudoPrimordialService;
import com.boss_battle.service.ativar_equipar.ResultadoDano;
import com.boss_battle.service.global_boss.GlobalBossService;


@Service
@Transactional
public class BossAutoAttackService {
	
	private String ultimaMensagemAtaque;
	private LocalDateTime ultimoAtaqueEm;
	private String ultimoBossNome;
	private Long ultimoDano;
	
	private final long ESCUDO_PRIMORDIAL_PORCENTAGEM = 40;


    private final GlobalBossService globalBossService;
    private final UsuarioBossBattleRepository usuarioRepo;
    private final EscudoPrimordialService escudoPrimordialService;

    public BossAutoAttackService(
            GlobalBossService globalBossService,
            UsuarioBossBattleRepository usuarioRepo,
            EscudoPrimordialService escudoPrimordialService 
    ) {
        this.globalBossService = globalBossService;
        this.usuarioRepo = usuarioRepo;
        this.escudoPrimordialService = escudoPrimordialService;
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

                ResultadoDano resultado = calcularDanoRecebido(u, dano);

                long novaEnergia = u.getEnergiaGuerreiros() - resultado.getDanoFinal();

                u.setEnergiaGuerreiros(Math.max(0, novaEnergia));
                usuarioRepo.save(u);

                if (resultado.isUsouEscudo()) {
                	/*
                    System.out.println("🛡️ Usuário " + u.getId()
                            + " usou Escudo → recebeu " + resultado.getDanoFinal());
                            /*
                } else {
                /*
                    System.out.println("💥 Usuário " + u.getId()
                            + " recebeu " + resultado.getDanoFinal());
                            */
                }
            }
        }

        String bossName = boss.getBossName();

        /*
        System.out.println(bossName +
                " atacou causando " + df.format(dano) + " de dano!");
*/
        String mensagem = "<span class='boosName'>" + bossName + "</span>"
                + " atacou causando <span class='dano-valor'>"
                + df.format(dano)
                + "</span> de dano!";

      
        ultimoBossNome = bossName;
        ultimoAtaqueEm = LocalDateTime.now();
        ultimaMensagemAtaque = mensagem;
        ultimoDano = dano;
        

        return new AtaqueBossResponseDTO(
                ultimoBossNome,
                ultimaMensagemAtaque,
                ultimoDano,
                ultimoAtaqueEm
        );
    }
    
    //-----------------------------------------------------------------
    /*
    public AtaqueBossResponseDTO executarAtaque(BattleBoss boss) {
    	 DecimalFormat df = new DecimalFormat("#,##0");
    	 
    	 
    	   long dano = boss.getAttackPower();

        List<UsuarioBossBattle> usuarios = usuarioRepo.findAll();

        for (UsuarioBossBattle u : usuarios) {

            if (u.getEnergiaGuerreiros() != null && u.getEnergiaGuerreiros() > 0) {

                ResultadoDano resultado = calcularDanoRecebido(u, dano);

                long novaEnergia = u.getEnergiaGuerreiros() - resultado.getDanoFinal();

                u.setEnergiaGuerreiros(Math.max(0, novaEnergia));
                usuarioRepo.save(u);

                if (resultado.isUsouEscudo()) {
                    System.out.println("🛡️ Usuário " + u.getId()
                            + " usou Escudo → recebeu " + resultado.getDanoFinal());
                } else {
                    System.out.println("💥 Usuário " + u.getId()
                            + " recebeu " + resultado.getDanoFinal());
                }
            }
        }
        
        
        //-------------------------------------
         String boosName  = boss.getBossName();
        
        System.out.println( boosName+
                " atacou causando " + df.format(dano) + " de dano!");
        
        
        String mensagem = "<span class='boosName'>" + boosName + "</span>"
                + " atacou causando <span class='dano-valor'>"
                + df.format(dano)
                + "</span> de dano!";

        
        String mensagem = boss.getBossName()
                + " atacou causando " + df.format(dano) + " de dano!";

        ultimoAtaqueEm = LocalDateTime.now();
        ultimaMensagemAtaque = mensagem;

        return new AtaqueBossResponseDTO(
                boss.getBossName(),
                dano,
                LocalDateTime.now()
        );
    }
    
   */ 

private ResultadoDano calcularDanoRecebido(UsuarioBossBattle usuario, long danoBase) {

    boolean usouEscudo = escudoPrimordialService.usarEscudoPrimordial(usuario);
    
    /*
     * debud
    System.out.println("Usuário: " + usuario.getId()
    + " | Escudo ativo: " + usuario.getEscudoPrimordialAtivo()
    + " | Desgaste: " + usuario.getEscudoPrimordialDesgaste()
    + " | Usou escudo: " + usouEscudo);
*/

    if (usouEscudo) {
        long reducao = (danoBase * ESCUDO_PRIMORDIAL_PORCENTAGEM) / 100;
        long danoFinal = Math.max(0, danoBase - reducao);

        return new ResultadoDano(danoFinal, true);
    }

    return new ResultadoDano(danoBase, false);
}

public AtaqueBossResponseDTO getUltimoAtaque() {

    if (ultimoAtaqueEm == null) return null;

    return new AtaqueBossResponseDTO(
            ultimoBossNome,
            ultimaMensagemAtaque,
            ultimoDano,
            ultimoAtaqueEm
    );
}

/*
    public AtaqueBossResponseDTO getUltimoAtaque() {

        if (ultimoAtaqueEm == null) return null;

        return new AtaqueBossResponseDTO(
                ultimaMensagemAtaque,
                null,
                ultimoAtaqueEm
        );
    }

*/
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



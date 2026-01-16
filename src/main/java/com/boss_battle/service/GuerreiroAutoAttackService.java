
package com.boss_battle.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.boss_battle.model.BattleBoss;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

import jakarta.transaction.Transactional;

@Service
public class GuerreiroAutoAttackService {

	private final long ESPADA_FLANEJANTE_PROCENTGEM = 20;
	private final long MACHADO_DILACERADOR_PROCENTGEM = 10;

	@Autowired
	private EspadaFlanejanteService espadaFlanejanteService;

	@Autowired
	private UsuarioBossBattleRepository repo;

	@Autowired
	private MachadoDilaceradorService machadoDilaceradorService;
	
	
	@Autowired
	PocaoVigorService pocaoVigorService;

	@Autowired
	GlobalBossService globalBossService;

	@Transactional(Transactional.TxType.REQUIRES_NEW)
	public void processarAtaqueUsuario(UsuarioBossBattle usuario) {

	    Long guerreiros = usuario.getGuerreiros();
	    Long ataqueBase = usuario.getAtaqueBaseGuerreiros();
	    Long energia = usuario.getEnergiaGuerreiros();
	    Long espadasAtivas = usuario.getEspadaFlanejanteAtiva();
	    Long machadoDilaceradorAtivo = usuario.getMachadoDilaceradorAtivo();

	    BattleBoss boss = globalBossService.getActiveBoss();
        if (boss == null || !boss.isAlive()) return;
        
        
	    if (energia == null || energia <= 0) return;
	    if (guerreiros == null || guerreiros <= 0) return;

	    if (ataqueBase == null || ataqueBase <= 0) ataqueBase = 1L;

	    // ⚔️ dano base
	    long dano = guerreiros * ataqueBase;

	    // ⚔️ bônus da espada (20%)
	    if (espadasAtivas != null && espadasAtivas > 0) {

	        long bonusEspada = (dano * ESPADA_FLANEJANTE_PROCENTGEM) / 100;
	        dano += bonusEspada;

	        // 🔥 consome desgaste
	        espadaFlanejanteService.usarEspadaFlanejante(usuario);
	    }
	    
	    // ⚔️ bônus do MCAHDO DILACERADOR (10%)
	    if (machadoDilaceradorAtivo != null && machadoDilaceradorAtivo > 0) {

	        long bonusMachado = (dano * MACHADO_DILACERADOR_PROCENTGEM) / 100;
	        dano += bonusMachado;

	        // 🔥 consome desgaste
	        machadoDilaceradorService.usarMachadoDilacerador(usuario);
	    }
	    
	    globalBossService.tryHitBoss(boss.getBossName(), boss, usuario, dano);
	    
	   System.out.println("Usario" +"-"+ usuario.getUsername()+"-"+  "Atacou" +"-"+  boss.getBossName()+"-"+  "Causou " + dano+"-"+ "Dano");
	  
	    // 🐲 ataca o boss
	   // globalBossService.hitActiveBoss(usuario, dano);

	    // 🔋 consome energia
	    long energiaFinal = energia - dano;
	    if (energiaFinal < 0) energiaFinal = 0;
	    usuario.setEnergiaGuerreiros(energiaFinal);

	    // 🧪 poção automática
	    pocaoVigorService.verificarEUsarPocaoSeAtiva(usuario);

	    repo.save(usuario);
	}


@Scheduled(fixedRate = 60000)
public void ataqueAutomatico() {

    //List<UsuarioBossBattle> usuarios = repo.findAll();
    List<UsuarioBossBattle> usuarios = repo.buscarUsuariosAtivos();

    for (UsuarioBossBattle usuario : usuarios) {
        try {
            processarAtaqueUsuario(usuario);
        } catch (Exception e) {
            // loga e segue o jogo
        }
    }
}

}


/**
package com.boss_battle.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.boss_battle.controller.GlobalBossController;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

import jakarta.transaction.Transactional;

@Service
public class GuerreiroAutoAttackService {

    @Autowired
    private UsuarioBossBattleRepository repo;

    @Autowired
    PocaoVigorService pocaoVigorService;
    
    @Autowired
    GlobalBossService globalBossService;
    
  
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void ataqueAutomatico() {

        List<UsuarioBossBattle> usuarios = repo.findAll();

        for (UsuarioBossBattle usuario : usuarios) {

            Long guerreiros = usuario.getGuerreiros();
            Long ataqueBase = usuario.getAtaqueBaseGuerreiros();
            Long energia = usuario.getEnergiaGuerreiros();
            
            if (energia == null) energia = 0L;

            // ❌ Sem guerreiros ou sem energia
            if (guerreiros == null || guerreiros <= 0) continue;
            if (energia == null || energia <= 0) continue;

            if (ataqueBase == null || ataqueBase <= 0) ataqueBase = 1L;

            
            long dano = guerreiros * ataqueBase;

            // 🔥 Ataca o boss
            globalBossService.atacarBossAtivo(usuario, dano);


          
            usuario.setEnergiaGuerreiros(energia -dano);
            
           
            pocaoVigorService.verificarEUsarPocaoSeAtiva(usuario);

            repo.save(usuario);
        }
    }

}
*/
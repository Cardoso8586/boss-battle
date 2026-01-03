
package com.boss_battle.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
	    
	    @Transactional(Transactional.TxType.REQUIRES_NEW)
	    public void processarAtaqueUsuario(UsuarioBossBattle usuario) {

	        Long guerreiros = usuario.getGuerreiros();
	        Long ataqueBase = usuario.getAtaqueBaseGuerreiros();
	        Long energia = usuario.getEnergiaGuerreiros();

	        if (energia == null || energia <= 0) return;
	        if (guerreiros == null || guerreiros <= 0) return;

	        if (ataqueBase == null || ataqueBase <= 0) ataqueBase = 1L;

	        long dano = guerreiros * ataqueBase;

	        globalBossService.atacarBossAtivo(usuario, dano);

	        // 🔒 trava energia para nunca voltar a atacar
	        long energiaFinal = energia - dano;
	        if (energiaFinal < 0) energiaFinal = 0;

	        usuario.setEnergiaGuerreiros(energiaFinal);

	        pocaoVigorService.verificarEUsarPocaoSeAtiva(usuario);

	        repo.save(usuario);
	    }
   
	  /**  
@Transactional(Transactional.TxType.REQUIRES_NEW)
public void processarAtaqueUsuario(UsuarioBossBattle usuario) {

    Long guerreiros = usuario.getGuerreiros();
    Long ataqueBase = usuario.getAtaqueBaseGuerreiros();
    Long energia = usuario.getEnergiaGuerreiros();

    if (energia == null || energia <= 0) return;
    if (guerreiros == null || guerreiros <= 0) return;

    if (ataqueBase == null || ataqueBase <= 0) ataqueBase = 1L;

    long dano = guerreiros * ataqueBase;

    globalBossService.atacarBossAtivo(usuario, dano);

    usuario.setEnergiaGuerreiros(energia - dano);

    pocaoVigorService.verificarEUsarPocaoSeAtiva(usuario);

    repo.save(usuario);
}
*/
@Scheduled(fixedRate = 60000)
public void ataqueAutomatico() {

    List<UsuarioBossBattle> usuarios = repo.findAll();

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
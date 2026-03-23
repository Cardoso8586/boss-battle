package com.boss_battle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.service.aprimoramentos_loja.LojaAprimoramentosService;

@Service
@Transactional
public class UsuarioBossBattleService {

    @Autowired
    private LojaAprimoramentosService lojaService;
    @Autowired
    private RandomRewardService randomRewardService;
   // private static final long XP_POR_NIVEL = 1000;

    public void adicionarExp(UsuarioBossBattle usuario, long expGanha) {

        usuario.setExp(usuario.getExp() + expGanha);

        while (true) {

        	
        	//---> AUMENTA A DIFICULDADE DE SUBIR DE NIVEL
            long nivelUsuario = usuario.getNivel();
            long xpPorNivel = 1000;

            if (nivelUsuario < 100) xpPorNivel = 1000;
            else if (nivelUsuario < 200) xpPorNivel = 1100;
            else if (nivelUsuario < 300) xpPorNivel = 1200;
            else if (nivelUsuario < 400) xpPorNivel = 1300;
            else if (nivelUsuario < 500) xpPorNivel = 1400;
            else if (nivelUsuario < 600) xpPorNivel = 1500;
            else if (nivelUsuario < 700) xpPorNivel = 1600;
            else if (nivelUsuario < 800) xpPorNivel = 1700;
            else if (nivelUsuario < 900) xpPorNivel = 1800;
            else if (nivelUsuario < 1000) xpPorNivel = 1900;

            // 🔥 MID GAME
            else if (nivelUsuario < 1500) xpPorNivel = 2100;
            else if (nivelUsuario < 2000) xpPorNivel = 2300;
            else if (nivelUsuario < 2500) xpPorNivel = 2500;
            else if (nivelUsuario < 3000) xpPorNivel = 2700;
            else if (nivelUsuario < 3500) xpPorNivel = 2900;
            else if (nivelUsuario < 4000) xpPorNivel = 3100;

            // 🔥 LATE GAME
            else if (nivelUsuario < 5000) xpPorNivel = 3400;
            else if (nivelUsuario < 6000) xpPorNivel = 3700;
            else if (nivelUsuario < 7000) xpPorNivel = 4000;
            else if (nivelUsuario < 8000) xpPorNivel = 4300;
            else if (nivelUsuario < 9000) xpPorNivel = 4600;
            else if (nivelUsuario < 10000) xpPorNivel = 5000;

            // 🔥 ENDGAME
            else xpPorNivel = 5500;

            if (usuario.getExp() < xpPorNivel) {
                break;
            }

            usuario.setExp(usuario.getExp() - xpPorNivel);
            usuario.setNivel(usuario.getNivel() + 1);

            usuario.setAtaqueBase(usuario.getAtaqueBase() + 1);

            randomRewardService.onLevelUp(usuario);
        }
    }
    /*
    public void adicionarExp(UsuarioBossBattle usuario, long expGanha) {

        // ➕ Adiciona XP
        usuario.setExp(usuario.getExp() + expGanha);
        
        long xpPorNivel = 1000 ;
        long nivelUsuario = usuario.getNivel();
        
        if(nivelUsuario < 100) {
        	xpPorNivel = 1000;
        }else if(nivelUsuario < 200) {
        	xpPorNivel = 1100;
        }else if(nivelUsuario < 300) {
        	xpPorNivel = 1200;
        }else if(nivelUsuario < 300) {
        	xpPorNivel = 1300;
        }else if(nivelUsuario < 400) {
        	xpPorNivel = 1400;
        }else if(nivelUsuario < 500) {
        	xpPorNivel = 1500;
        }else if(nivelUsuario < 600) {
        	xpPorNivel = 1600;
        }else if(nivelUsuario < 700) {
        	xpPorNivel = 1700;
        }else if(nivelUsuario < 800) {
        	xpPorNivel = 1800;
        }else if(nivelUsuario < 900) {
        	xpPorNivel = 1900;
        }else if(nivelUsuario < 2000) {
        	xpPorNivel = 2500;
        }else if(nivelUsuario < 3000) {
        	xpPorNivel = 3000;
        }else if(nivelUsuario < 4000) {
        	xpPorNivel = 3500;
        }
        
        
        
        // 🔁 Sobe nível se necessário
        while (usuario.getExp() >= xpPorNivel) {
            usuario.setExp(usuario.getExp() - xpPorNivel);
            usuario.setNivel(usuario.getNivel() + 1);

            // Buffs por nível
            usuario.setAtaqueBase(usuario.getAtaqueBase() + 1);
            
            
         // 🎲 gera o próximo preview
            randomRewardService.onLevelUp(usuario);
           // lojaService.atualizarPrecoLoja(usuario);
           
        }

       // repo.save(usuario);
    }
    */
    
    
}

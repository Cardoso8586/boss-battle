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

            if (nivelUsuario < 25) xpPorNivel = 1000;
            else if (nivelUsuario < 50) xpPorNivel = 1100;
            else if (nivelUsuario < 75) xpPorNivel = 1200;
            else if (nivelUsuario < 100) xpPorNivel = 1300;
            else if (nivelUsuario < 125) xpPorNivel = 1400;
            else if (nivelUsuario < 150) xpPorNivel = 1500;
            else if (nivelUsuario < 175) xpPorNivel = 1600;
            else if (nivelUsuario < 200) xpPorNivel = 1800;
            else if (nivelUsuario < 225) xpPorNivel = 1900;
            else if (nivelUsuario < 250) xpPorNivel = 2000;

            // 🔥 MID GAME
            else if (nivelUsuario < 275) xpPorNivel = 2100;
            else if (nivelUsuario < 300) xpPorNivel = 2200;
            else if (nivelUsuario < 325) xpPorNivel = 2300;
            else if (nivelUsuario < 350) xpPorNivel = 2400;
            else if (nivelUsuario < 375) xpPorNivel = 2500;
            else if (nivelUsuario < 400) xpPorNivel = 2600;

            // 🔥 LATE GAME
            else if (nivelUsuario < 425) xpPorNivel = 2700;
            else if (nivelUsuario < 450) xpPorNivel = 2800;
            else if (nivelUsuario < 475) xpPorNivel = 2900;
            else if (nivelUsuario < 500) xpPorNivel = 3000;
            else if (nivelUsuario < 525) xpPorNivel = 3100;
            else if (nivelUsuario < 550) xpPorNivel = 3200;
         // 🔥 CONTINUAÇÃO
       
            else if (nivelUsuario < 575) xpPorNivel = 3350;
            else if (nivelUsuario < 600) xpPorNivel = 3450;
            else if (nivelUsuario < 625) xpPorNivel = 3550;
            else if (nivelUsuario < 650) xpPorNivel = 3650;
            else if (nivelUsuario < 675) xpPorNivel = 3750;
            else if (nivelUsuario < 700) xpPorNivel = 3850;

            else if (nivelUsuario < 725) xpPorNivel = 3950;
            else if (nivelUsuario < 750) xpPorNivel = 4050;
            else if (nivelUsuario < 775) xpPorNivel = 4150;
            else if (nivelUsuario < 800) xpPorNivel = 4250;
            else if (nivelUsuario < 825) xpPorNivel = 4350;
            else if (nivelUsuario < 850) xpPorNivel = 4450;

            else if (nivelUsuario < 875) xpPorNivel = 4550;
            else if (nivelUsuario < 900) xpPorNivel = 4650;
            else if (nivelUsuario < 925) xpPorNivel = 4750;
            else if (nivelUsuario < 950) xpPorNivel = 4850;
            else if (nivelUsuario < 975) xpPorNivel = 4950;
            else if (nivelUsuario < 1000) xpPorNivel = 5050;

            // 🔥 SEGUE PADRÃO (sem pulo)
            else if (nivelUsuario < 1025) xpPorNivel = 5150;
            else if (nivelUsuario < 1050) xpPorNivel = 5250;
            else if (nivelUsuario < 1075) xpPorNivel = 5350;
            else if (nivelUsuario < 1100) xpPorNivel = 5450;
            else if (nivelUsuario < 1125) xpPorNivel = 5550;
            else if (nivelUsuario < 1150) xpPorNivel = 5650;

            else if (nivelUsuario < 1175) xpPorNivel = 5750;
            else if (nivelUsuario < 1200) xpPorNivel = 5850;
            else if (nivelUsuario < 1225) xpPorNivel = 5950;
            else if (nivelUsuario < 1250) xpPorNivel = 6050;
            // 🔥 ENDGAME
            else xpPorNivel = 6500;

            if (usuario.getExp() < xpPorNivel) {
                break;
            }

            usuario.setExp(usuario.getExp() - xpPorNivel);
            usuario.setNivel(usuario.getNivel() + 1);

            usuario.setAtaqueBase(usuario.getAtaqueBase() + 1);

            randomRewardService.onLevelUp(usuario);
        }
    }

    
    
}

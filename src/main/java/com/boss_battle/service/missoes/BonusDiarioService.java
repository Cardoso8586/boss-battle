package com.boss_battle.service.missoes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.BossBonusDiario;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.BonusDiarioRepository;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.service.UltimoValorRecebidoService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BonusDiarioService {

    private final Random random = new Random();

    @Autowired
    private UltimoValorRecebidoService ultimoValorRecebidoService;
    
    @Autowired
    private BonusDiarioRepository bonusRepository;

    @Autowired
    private UsuarioBossBattleRepository usuarioRepository;

    // Valor do bônus baseado no streak
    private BigDecimal getValorBonusPorStreak(int streak) {

        switch (streak) {
            case 1: return new BigDecimal("300");
            case 2: return new BigDecimal("400");
            case 3: return new BigDecimal("500");
            case 4: return new BigDecimal("600");
            case 5: return new BigDecimal("700");
            case 6: return new BigDecimal("800");
            case 7: return new BigDecimal("1000");
            default: return new BigDecimal("1000");
        }
    }

    public String coletarBonusDiario(Long usuarioId) {

        UsuarioBossBattle usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        LocalDate hoje = LocalDate.now();

        // Verifica se já coletou hoje
        Optional<BossBonusDiario> bonusHoje =
                bonusRepository.findByUsuarioAndDataColeta(usuario, hoje);

        if (bonusHoje.isPresent()) {
            return "Você já coletou seu bônus diário hoje!";
        }

        // Busca último bônus
        Optional<BossBonusDiario> ultimoBonusOpt =
                bonusRepository.findTopByUsuarioAndDataColetaBeforeOrderByDataColetaDesc(usuario, hoje);

        int streak = 1;

        if (ultimoBonusOpt.isPresent()) {

            BossBonusDiario ultimoBonus = ultimoBonusOpt.get();

            if (ultimoBonus.getDataColeta().equals(hoje.minusDays(1))) {

                int ultimoStreak = ultimoBonus.getStreak() == null ? 0 : ultimoBonus.getStreak();
                streak = Math.min(ultimoStreak + 1, 7);

            } else {
                streak = 1;
            }
        }

        BigDecimal valorBonus = getValorBonusPorStreak(streak);

        BigDecimal saldoAtual = usuario.getBossCoins();

        if (saldoAtual == null) {
            saldoAtual = BigDecimal.ZERO;
        }

        usuario.setBossCoins(saldoAtual.add(valorBonus));
        ultimoValorRecebidoService.setUltimoValorRecebido(usuario, valorBonus);

        usuarioRepository.save(usuario);

        // Salva histórico do bônus
        BossBonusDiario bonus = new BossBonusDiario();
        bonus.setUsuario(usuario);
        bonus.setDataColeta(hoje);
        bonus.setStreak(streak);

        bonusRepository.save(bonus);

        String mensagem = valorBonus + " Boss Coins coletado com sucesso!";

        if (streak == 7) {

            String[] mensagens = {
                "🔥 Guerreiro lendário! 7 dias seguidos enfrentando batalhas!",
                "⚔️ Você provou seu valor! 7 dias de batalhas contra os bosses!",
                "🏆 Vitória épica! 7 dias consecutivos!",
                "🛡️ Determinação de campeão!",
                "👑 Herói verdadeiro!",
                "💀 Nem os bosses te pararam!",
                "🔥 A lenda cresce!",
                "⚡ Seu poder aumenta!",
                "🎯 Domínio total da arena!",
                "🐉 Os bosses temem seu nome!"
            };

            return mensagem + " " + mensagens[random.nextInt(mensagens.length)];
        }

        return mensagem + " " + streak + (streak == 1 ? " dia " : " dias seguidos");
    }

    // Verifica se o usuário pode coletar hoje
    public boolean verificarDisponibilidade(Long usuarioId) {

        UsuarioBossBattle usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        LocalDate hoje = LocalDate.now();

        return bonusRepository.findByUsuarioAndDataColeta(usuario, hoje).isEmpty();
    }

}

/*
package com.boss_battle.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.BossBonusDiario;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.BonusDiarioRepository;
import com.boss_battle.repository.UsuarioBossBattleRepository;


import jakarta.transaction.Transactional;

@Service
@Transactional
public class BonusDiarioService {
	
	    private final Random random = new Random();
	    @Autowired
	    private BonusDiarioRepository bonusRepository;
	 
	    @Autowired
	    private UsuarioBossBattleRepository repo;

	    private BigDecimal getValorBonusPorStreak(int streak) {
	        switch (streak) {
	            case 1: return new BigDecimal("300");
	            case 2: return new BigDecimal("400");
	            case 3: return new BigDecimal("500");
	            case 4: return new BigDecimal("600");
	            case 5: return new BigDecimal("700");
	            case 6: return new BigDecimal("800");
	            case 7: return new BigDecimal("1000");
	            default: return new BigDecimal("1000");
	        }
	        
	    }//getValorBonusPorStreak
	    
	    
	    
	  public String coletarBonusDiario(Long usuarioId) {
		// 🔒 Busca usuário com lock pessimista
		  UsuarioBossBattle usuario = repo.findById(usuarioId)
			        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
		  
	        LocalDate hoje = LocalDate.now();
		  
	        Optional<BossBonusDiario> bonusHoje = bonusRepository.findByUsuarioAndDataColeta(usuario, hoje);
	        if (bonusHoje.isPresent()) {
	            return "Você já coletou seu bônus diário hoje!";
	        }
		  
	        Optional<BossBonusDiario> ultimoBonusOpt = bonusRepository.findTopByUsuarioAndDataColetaBeforeOrderByDataColetaDesc(usuario, hoje);
		  
	        int streak = 1;

	        if (ultimoBonusOpt.isPresent()) {

	            BossBonusDiario ultimoBonus = ultimoBonusOpt.get();

	            if (ultimoBonus.getDataColeta().equals(hoje.minusDays(1))) {

	                Integer ultimoStreak = ultimoBonus.getStreak();

	                if (ultimoStreak == null) {
	                    ultimoStreak = 0;
	                }

	                streak = Math.min(ultimoStreak + 1, 7);

	            } else {

	                streak = 1;
	            }
	        }

	        BigDecimal valorBonus = getValorBonusPorStreak(streak);

	        BigDecimal saldoAtual = usuario.getBossCoins();

	        if (saldoAtual == null) {
	            saldoAtual = BigDecimal.ZERO;
	        }

	        usuario.setBossCoins(saldoAtual.add(valorBonus));

	        repo.save(usuario);

	        String mensagem = valorBonus + " Boss Coins coletado com sucesso!";

	        if (streak == 7) {

	        	String[] mensagens = {
	        		    "🔥 Guerreiro lendário! 7 dias seguidos enfrentando batalhas! Continue conquistando recompensas!",
	        		    "⚔️ Você provou seu valor! 7 dias de batalhas diárias contra os chefes!",
	        		    "🏆 Vitória épica! 7 dias consecutivos derrotando desafios!",
	        		    "🛡️ Sua determinação é digna de um campeão! 7 dias seguidos na arena!",
	        		    "👑 Herói verdadeiro! 7 dias enfrentando bosses e ficando mais forte!",
	        		    "💀 Nem os bosses conseguiram te parar! 7 dias seguidos de conquistas!",
	        		    "🔥 A lenda cresce! 7 dias consecutivos coletando recompensas de batalha!",
	        		    "⚡ Seu poder está aumentando! 7 dias enfrentando os desafios do Boss Battle!",
	        		    "🎯 Você domina a arena! 7 dias seguidos de vitórias!",
	        		    "🐉 Os bosses já temem seu nome! 7 dias consecutivos de batalha!"
	        		};

	            return mensagem + " (" + mensagens[random.nextInt(mensagens.length)] + ")";
	        }

	        return mensagem + " " + streak + (streak == 1 ? " dia " : " dias seguidos");
		  
	  }//coletarBonusDiario
	  public boolean verificarDisponibilidade(Long usuarioId) {
		  UsuarioBossBattle usuario = repo.findById(usuarioId)
			        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
		  
	        LocalDate hoje = LocalDate.now();

	        return bonusRepository.findByUsuarioAndDataColeta(usuario, hoje).isEmpty();
	    }
	

}//BonusDiarioService
*/
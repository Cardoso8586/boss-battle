package com.boss_battle.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossReflexa;
import com.boss_battle.repository.ReflexaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReflexaService {

    @Autowired
    private ReflexaRepository repo;

    /**
     * Retorna o boss com ID fixo 1
     */
    public GlobalBossReflexa get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    /**
     * Cria o boss caso não exista na base
     */
    public GlobalBossReflexa createDefaultBoss() {
        GlobalBossReflexa boss = new GlobalBossReflexa();

        boss.setName("REFLEXA");
        boss.setMaxHp(170_000L);
        boss.setCurrentHp(170_000L);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_reflexa.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(4800L);
        boss.setSpawnCount(1);
        boss.setAttackIntervalSeconds(146L);
        boss.setRewardBoss(90_000L);
        boss.setRewardExp(2400L);

        return repo.save(boss);
    }

    /**
     * Apenas salva alterações
     */
    public GlobalBossReflexa save(GlobalBossReflexa boss) {
        return repo.save(boss);
    }

    /**
     * Ataca o boss e salva os dados atualizados
     */
    public GlobalBossReflexa attack(long damage) {
        GlobalBossReflexa boss = repo.findById(1L)
            .orElseThrow(() -> new RuntimeException("Boss REFLEXA não encontrado"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }
    
    //===========================================================
    //incrmentar hp, toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoReflexa(GlobalBossReflexa boss) {


        Random random = new Random();
    	long min = 50;
    	long max = 200;
    	long incrementarUp = random.nextLong(min, max + 1);

    	
    	long valorHpMax =  boss.getMaxHp();
    	long valorCur = boss.getCurrentHp();
    	
    	boss.setMaxHp( valorHpMax + incrementarUp);
    	boss.setCurrentHp( valorCur + incrementarUp);
    	
    	long valorXp =  boss.getRewardExp();
    	boss.setRewardExp(valorXp + 6);
    	
    	long valorsetRewardBoss = boss.getRewardBoss();
    	boss.setRewardBoss(valorsetRewardBoss + 3);
    	
    	 //ataque respaw
        boss.setAttackPower(boss.getAttackPower() + 2);
        boss.setAttackIntervalSeconds(boss.getAttackIntervalSeconds() + 1);
 	   
    }//--->incrmentar hp, toda vez que o boss for derrotado
}

package com.boss_battle.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossOblivar;
import com.boss_battle.repository.OblivarRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OblivarService {

    @Autowired
    private OblivarRepository repo;

    /**
     * Retorna o boss com ID fixo 1
     */
    public GlobalBossOblivar get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    /**
     * Cria o boss caso não exista na base
     */
    public GlobalBossOblivar createDefaultBoss() {
    	GlobalBossOblivar boss = new GlobalBossOblivar();

        boss.setName("OBLIVAR");
        boss.setMaxHp(35_000);
        boss.setCurrentHp(35_000);
        boss.setProcessingDeath(false);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_oblivar.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(4200L); // 1 hora
        boss.setSpawnCount(1);
        boss.setAttackIntervalSeconds(146L);
        boss.setRewardBoss(30_000);
        boss.setRewardExp(1650);

        return repo.save(boss);
    }

    /**
     * Apenas salva alterações
     */
    public GlobalBossOblivar save(GlobalBossOblivar boss) {
        return repo.save(boss);
    }

    /**
     * Ataca o boss e salva os dados atualizados
     */
    public GlobalBossOblivar attack(long damage) {
    	GlobalBossOblivar boss = repo.findById(1L)
            .orElseThrow(() -> new RuntimeException("Boss Azurion não encontrado"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }
    
    //===========================================================
    //incrmentar hp, toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoOblivar(GlobalBossOblivar boss) {


        Random random = new Random();
    	long min = 25;
    	long max = 150;
    	long incrementarUp = random.nextLong(min, max + 1);

    	
    	long valorHpMax =  boss.getMaxHp();
    	long valorCur = boss.getCurrentHp();
    	
    	boss.setMaxHp( valorHpMax + incrementarUp);
    	boss.setCurrentHp( valorCur + incrementarUp);
    	
    	long valorXp =  boss.getRewardExp();
    	boss.setRewardExp(valorXp + 2);
    	
    	long valorsetRewardBoss = boss.getRewardBoss();
    	boss.setRewardBoss(valorsetRewardBoss + 2);
    	
    	 //ataque respaw
        boss.setAttackPower(boss.getAttackPower() + 2);
        boss.setAttackIntervalSeconds(boss.getAttackIntervalSeconds() + 1);
 	   
    }//--->incrmentar hp, toda vez que o boss for derrotado
}
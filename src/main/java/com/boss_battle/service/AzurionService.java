package com.boss_battle.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossAzurion;
import com.boss_battle.repository.AzurionRepository;

@Service
public class AzurionService {

    @Autowired
    private AzurionRepository repo;

    /**
     * Retorna o boss com ID fixo 1
     */
    public GlobalBossAzurion get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    /**
     * Cria o boss caso não exista na base
     */
    public GlobalBossAzurion createDefaultBoss() {
        GlobalBossAzurion boss = new GlobalBossAzurion();

        boss.setName("AZURION");
        boss.setMaxHp(100_000);
        boss.setCurrentHp(100_000);
        boss.setProcessingDeath(false);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_azurion.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(3600); // 1 hora
        boss.setAttackPower(120L);
        boss.setSpawnCount(1);
          boss.setAttackIntervalSeconds(160L);
        boss.setRewardBoss(35_000);
        boss.setRewardExp(1300);

        return repo.save(boss);
    }

    /**
     * Apenas salva alterações
     */
    public GlobalBossAzurion save(GlobalBossAzurion boss) {
        return repo.save(boss);
    }

    /**
     * Ataca o boss e salva os dados atualizados
     */
    public GlobalBossAzurion attack(long damage) {
        GlobalBossAzurion boss = repo.findById(1L)
            .orElseThrow(() -> new RuntimeException("Boss Azurion não encontrado"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }
    
    
    public void aplicarEscalamentoAzurion (GlobalBossAzurion boss) {


        Random random = new Random();
    	long min = 10;
    	long max = 150;
    	long incrementarUp = random.nextLong(min, max + 1);

    	
    	long valorHpMax =  boss.getMaxHp();
    	long valorCur = boss.getCurrentHp();
    	
    	boss.setMaxHp( valorHpMax + incrementarUp);
    	boss.setCurrentHp( valorCur + incrementarUp);
    	
    	long valorXp =  boss.getRewardExp();
    	boss.setRewardExp(valorXp + 1);
    	
    	long valorsetRewardBoss = boss.getRewardBoss();
    	boss.setRewardBoss(valorsetRewardBoss + 1);
    	
    	 //ataque respaw
        boss.setAttackPower(boss.getAttackPower() + 2);
        boss.setAttackIntervalSeconds(boss.getAttackIntervalSeconds() + 1);
 	   
    }//--->incrmentar hp, toda vez que o boss morrer
}

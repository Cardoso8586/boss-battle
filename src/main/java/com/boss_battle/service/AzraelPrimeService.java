package com.boss_battle.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossAzraelPrime;
import com.boss_battle.model.GlobalBossDestruidor;
import com.boss_battle.repository.AzraelPrimeRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AzraelPrimeService {

    @Autowired
    private AzraelPrimeRepository repo;

    // Sempre retorna o boss único (ID = 1)
    public GlobalBossAzraelPrime get() {
        return repo.findById(1L).orElseGet(this::createDefaultBoss);
    }

    // Cria o boss padrão
    public GlobalBossAzraelPrime createDefaultBoss() {
    	GlobalBossAzraelPrime boss = new GlobalBossAzraelPrime();

        boss.setName("AZRAEL PRIME");
        aplicarEscalamentoAzraelPrime(boss);
      //  boss.setMaxHp(400_000L);
       // boss.setCurrentHp(400_000L);
        boss.setProcessingDeath(false);
        boss.setAlive(true);

        boss.setAttackPower(2_400L);
        boss.setAttackIntervalSeconds(18L);

        boss.setImageUrl("images/boss_azrael_prime.webp");

        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(14_400L); 
        boss.setSpawnCount(1);

        boss.setRewardBoss(150_000L);
        boss.setRewardExp(10_000L);

        return repo.save(boss);
    }

    public GlobalBossAzraelPrime save(GlobalBossAzraelPrime boss) {
        return repo.save(boss);
    }

    // Ataque direto (usado em auto-attack ou debug)
    public GlobalBossAzraelPrime attack(long damage) {
    	GlobalBossAzraelPrime boss = get();
        boss.applyDamage(damage);
        return repo.save(boss);
    }
    
    //===========================================================
    //incrmentar hp, toda vez que o boss morrer
    //===========================================================
    
    public void aplicarEscalamentoAzraelPrime (GlobalBossAzraelPrime boss) {


        Random random = new Random();
    	long min = 50;
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
 	   
    }//--->incrmentar hp, toda vez que o boss morrer
}
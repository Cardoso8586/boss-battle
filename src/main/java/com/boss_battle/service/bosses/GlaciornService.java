package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossGlaciorn;
import com.boss_battle.repository.GlaciornRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class GlaciornService {

    @Autowired
    private GlaciornRepository repo;

    /**
     * Retorna o boss com ID fixo 1
     */
    public GlobalBossGlaciorn get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    /**
     * Cria o boss caso não exista na base
     */
    public GlobalBossGlaciorn createDefaultBoss() {
        GlobalBossGlaciorn boss = new GlobalBossGlaciorn();

        boss.setName("GLACIORN");
        boss.setMaxHp(120_000L);
        boss.setCurrentHp(120_000L);
        boss.setProcessingDeath(false);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_glaciorn.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(4000L); // ~1h06
        boss.setSpawnCount(1);
        boss.setAttackIntervalSeconds(180L);
        boss.setAttackPower(175L);
        boss.setRewardBoss(65_000L);
        boss.setRewardExp(1700L);

        return repo.save(boss);
    }

    /**
     * Apenas salva alterações
     */
    public GlobalBossGlaciorn save(GlobalBossGlaciorn boss) {
        return repo.save(boss);
    }

    /**
     * Ataca o boss e salva os dados atualizados
     */
    public GlobalBossGlaciorn attack(long damage) {
        GlobalBossGlaciorn boss = repo.findById(1L)
            .orElseThrow(() -> new RuntimeException("Boss Glaciorn não encontrado"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }
    
    //===========================================================
    //incrmentar hp, toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoGlaciorn(GlobalBossGlaciorn boss) {


        Random random = new Random();
    	long min = 10;
    	long max = 100;
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
     	long valorAtaque = boss.getAttackPower();
     	boss.setAttackPower(valorAtaque+ 6);
    	long valorIntervalSeconds= boss.getAttackIntervalSeconds();
        boss.setAttackIntervalSeconds(valorIntervalSeconds + 1);

 	   
    }//--->incrmentar hp, toda vez que o boss for derrotado
   
}


package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossGlaciara;
import com.boss_battle.repository.GlaciaraRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class GlaciaraService {

    @Autowired
    private GlaciaraRepository repo;

    public GlobalBossGlaciara get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossGlaciara createDefaultBoss() {
    	GlobalBossGlaciara boss = new GlobalBossGlaciara();
         boss.setName("GLACIARA - RAINHA DO GELO");
         boss.setMaxHp(65_000L);
         boss.setCurrentHp(65_000L);
         boss.setProcessingDeath(false);
         boss.setAlive(true);
         boss.setImageUrl("images/boss_glaciara.webp");
         boss.setSpawnedAt(LocalDateTime.now());
         boss.setRespawnCooldownSeconds(6000L);
         boss.setSpawnCount(1);
         //boss.setAttackPower(175L);
        /// boss.setAttackIntervalSeconds(190L);
         boss.setRewardBoss(60_000L);
         boss.setRewardExp(2400L);
        return repo.save(boss);
    }

    public GlobalBossGlaciara save(GlobalBossGlaciara boss) {
        return repo.save(boss);
    }
    
    public GlobalBossGlaciara attack(long damage) {
        var boss = repo.findById(1L)
                       .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss); // <- atualiza no banco
    }

    //===========================================================
    //incrmentar hp, toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoGlaciara(GlobalBossGlaciara boss) {


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

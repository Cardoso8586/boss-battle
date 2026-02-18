package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossMorvath;
import com.boss_battle.repository.MorvathRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MorvathService {

    @Autowired
    private MorvathRepository repo;

    public GlobalBossMorvath get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossMorvath createDefaultBoss() {
        GlobalBossMorvath boss = new GlobalBossMorvath();
        boss.setName("MORVATH");
        boss.setMaxHp(50_000L);
        boss.setCurrentHp(50_000L);
        boss.setProcessingDeath(false);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_morvath.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(5400); // 90 minutos
        boss.setSpawnCount(1);
        boss.setAttackPower(340L);
        boss.setAttackIntervalSeconds(238L);
        boss.setRewardBoss(50_000L);
        boss.setRewardExp(2200);
        return repo.save(boss);
    }

    public GlobalBossMorvath save(GlobalBossMorvath boss) {
        return repo.save(boss);
    }
    
    public GlobalBossMorvath attack(long damage) {
        var boss = repo.findById(1L)
                       .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss); // <- atualiza no banco
    }
    
  //===========================================================
    //incrmentar hp, toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoMorvat(GlobalBossMorvath boss) {


        Random random = new Random();
    	long min = 50;
    	long max = 200;
    	long incrementarUp = random.nextLong(min, max + 1);

    	
    	long valorHpMax =  boss.getMaxHp();
    	long valorCur = boss.getCurrentHp();
    	
    	boss.setMaxHp( valorHpMax + incrementarUp);
    	boss.setCurrentHp( valorCur + incrementarUp);
    	
    	long valorXp =  boss.getRewardExp();
    	boss.setRewardExp(valorXp + 4);
    	
    	long valorsetRewardBoss = boss.getRewardBoss();
    	boss.setRewardBoss(valorsetRewardBoss + 4);
 	   
    	 //ataque respaw
        boss.setAttackPower(boss.getAttackPower() + 2);
        boss.setAttackIntervalSeconds(boss.getAttackIntervalSeconds() + 1);
    }//--->incrmentar hp, toda vez que o boss for derrotado
}

package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossNoctyr;
import com.boss_battle.repository.NoctyrRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NoctyrService {

    @Autowired
    private NoctyrRepository repo;

    public GlobalBossNoctyr get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    
    public GlobalBossNoctyr createDefaultBoss() {
    	GlobalBossNoctyr boss = new GlobalBossNoctyr();
        boss.setName("NOCTYR");
        aplicarEscalamentoNoctyr(boss);
       // boss.setMaxHp(60_000);
       // boss.setCurrentHp(60_000);
        boss.setProcessingDeath(false);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_noctyr.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(5400);
        boss.setSpawnCount(1);
        boss.setAttackPower(160L);
        boss.setAttackIntervalSeconds(185L);
        boss.setRewardBoss(55_000); 
        boss.setRewardExp(2200);

        return repo.save(boss);
    }

    public GlobalBossNoctyr save(GlobalBossNoctyr boss) {
        return repo.save(boss);
    }

    public GlobalBossNoctyr attack(long damage) {
        var boss = repo.findById(1L)
                .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }
    
    
    //===========================================================
    //incrmentar hp, toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoNoctyr(GlobalBossNoctyr boss) {


        Random random = new Random();
    	long min = 50;
    	long max = 300;
    	long incrementarUp = random.nextLong(min, max + 1);

    	
    	long valorHpMax =  boss.getMaxHp();
    	long valorCur = boss.getCurrentHp();
    	
    	boss.setMaxHp( valorHpMax + incrementarUp);
    	boss.setCurrentHp( valorCur + incrementarUp);
    	
    	long valorXp =  boss.getRewardExp();
    	boss.setRewardExp(valorXp + 3);
    	
    	long valorsetRewardBoss = boss.getRewardBoss();
    	boss.setRewardBoss(valorsetRewardBoss + 3);
    	
    	 //ataque respaw
        boss.setAttackPower(boss.getAttackPower() + 2);
        boss.setAttackIntervalSeconds(boss.getAttackIntervalSeconds() + 1);
 	   
    }//--->incrmentar hp, toda vez que o boss for derrotado
}
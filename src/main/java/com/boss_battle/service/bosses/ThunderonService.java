package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossThunderon;
import com.boss_battle.repository.ThunderonRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ThunderonService {

    @Autowired
    private ThunderonRepository repo;

    public GlobalBossThunderon get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossThunderon createDefaultBoss() {
    	GlobalBossThunderon boss = new GlobalBossThunderon();
        boss.setName("THUNDERON");
        boss.setMaxHp(260_000L);
        boss.setCurrentHp(260_000L);
        boss.setProcessingDeath(false);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_thunderon.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(6000L); 
        boss.setSpawnCount(1);
        boss.setAttackIntervalSeconds(220L);
        boss.setAttackPower(160L);
        boss.setRewardBoss(140_000L);
        boss.setRewardExp(3600);
        return repo.save(boss);
    }

    public GlobalBossThunderon save(GlobalBossThunderon boss) {
        return repo.save(boss);
    }
    
    public GlobalBossThunderon attack(long damage) {
        var boss = repo.findById(1L)
                       .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss); // <- atualiza no banco
    }
    
    //===========================================================
    //incrmentar hp, toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoThunderon(GlobalBossThunderon boss) {


        Random random = new Random();
    	long min = 50;
    	long max = 100;
    	long incrementarUp = random.nextLong(min, max + 1);

    	
    	long valorHpMax =  boss.getMaxHp();
    	long valorCur = boss.getCurrentHp();
    	
    	boss.setMaxHp( valorHpMax + incrementarUp);
    	boss.setCurrentHp( valorCur + incrementarUp);
    	
    	long valorXp =  boss.getRewardExp();
    	boss.setRewardExp(valorXp + 5);
    	
    	long valorsetRewardBoss = boss.getRewardBoss();
    	boss.setRewardBoss(valorsetRewardBoss + 2);
 	   
    	 //ataque respaw
        boss.setAttackPower(boss.getAttackPower() + 2);
        boss.setAttackIntervalSeconds(boss.getAttackIntervalSeconds() + 1);
    }//--->incrmentar hp, toda vez que o boss for derrotado
}

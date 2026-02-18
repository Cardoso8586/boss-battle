package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossDrakthor;
import com.boss_battle.repository.DrakthorRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DrakthorService {

    @Autowired
    private DrakthorRepository repo;

    public GlobalBossDrakthor get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    
    public GlobalBossDrakthor createDefaultBoss() {
        GlobalBossDrakthor boss = new GlobalBossDrakthor();
        boss.setName("DRAKTHOR — O DEVORA-MUNDOS");
        boss.setMaxHp(150_000);
        boss.setCurrentHp(150_000);
        boss.setProcessingDeath(false);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_drakthor.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(7200);
        boss.setAttackIntervalSeconds(195L);
        boss.setAttackPower(120L);
        boss.setSpawnCount(1);
        boss.setRewardBoss(35_000); 
        boss.setRewardExp(1500);

        return repo.save(boss);
    }

    public GlobalBossDrakthor save(GlobalBossDrakthor boss) {
        return repo.save(boss);
    }

    public GlobalBossDrakthor attack(long damage) {
        var boss = repo.findById(1L)
                .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }//=============================================================
    
    public void aplicarEscalamentoDrakthor(GlobalBossDrakthor boss) {


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

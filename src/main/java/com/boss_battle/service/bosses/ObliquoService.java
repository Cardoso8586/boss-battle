package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossObliquo;
import com.boss_battle.repository.ObliquoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ObliquoService {

    @Autowired
    private ObliquoRepository repo;

    public GlobalBossObliquo get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossObliquo createDefaultBoss() {
        GlobalBossObliquo boss = new GlobalBossObliquo();
        boss.setName("OBLÍQUO");
        boss.setMaxHp(48_000L);
        boss.setCurrentHp(48_000L);
        boss.setProcessingDeath(false);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_obliquo.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(4800); // 80 minutos
        boss.setSpawnCount(1);
        //boss.setAttackIntervalSeconds(170L);
        //boss.setAttackPower(140L);
        boss.setRewardBoss(45_000L);
        boss.setRewardExp(2000);
        return repo.save(boss);
    }

    public GlobalBossObliquo save(GlobalBossObliquo boss) {
        return repo.save(boss);
    }
    
    public GlobalBossObliquo attack(long damage) {
        var boss = repo.findById(1L)
                       .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss); // atualiza no banco
    }
    
    //===========================================================
    //incrmentar hp, toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoObliquo(GlobalBossObliquo boss) {


        Random random = new Random();
    	long min = 50;
    	long max = 250;
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

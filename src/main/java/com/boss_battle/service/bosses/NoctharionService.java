package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossNoctharion;
import com.boss_battle.repository.NoctharionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NoctharionService {

    @Autowired
    private NoctharionRepository repo;

    // Sempre retorna o boss único (ID = 1)
    public GlobalBossNoctharion get() {
        return repo.findById(1L).orElseGet(this::createDefaultBoss);
    }

    // Cria o boss padrão
    public GlobalBossNoctharion createDefaultBoss() {
    	GlobalBossNoctharion boss = new GlobalBossNoctharion();

        boss.setName("NOCTHARION");
        boss.setMaxHp(180_000L);
        boss.setCurrentHp(180_000L);
        boss.setProcessingDeath(false);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_noctharion.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(5400); // 80 minutos
        boss.setSpawnCount(1);
        boss.setAttackPower(320L);
        boss.setAttackIntervalSeconds(230L);
        boss.setRewardBoss(120_000L);
        boss.setRewardExp(6200L);

        return repo.save(boss);
    }

    public GlobalBossNoctharion save(GlobalBossNoctharion boss) {
        return repo.save(boss);
    }

    // Ataque direto (usado em auto-attack ou debug)
    public GlobalBossNoctharion attack(long damage) {
    	GlobalBossNoctharion boss = get();
        boss.applyDamage(damage);
        return repo.save(boss);
    }
    
    //===========================================================
    //incrmentar hp, toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoNoctharion(GlobalBossNoctharion boss) {


        Random random = new Random();
    	long min = 100;
    	long max = 400;
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

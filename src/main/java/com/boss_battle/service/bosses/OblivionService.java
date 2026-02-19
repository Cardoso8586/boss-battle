package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossOblivion;
import com.boss_battle.repository.OblivionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OblivionService {

    @Autowired
    private OblivionRepository repo;

    // Sempre retorna o boss único (ID = 1)
    public GlobalBossOblivion get() {
        return repo.findById(1L).orElseGet(this::createDefaultBoss);
    }

    // Cria o boss padrão
    public GlobalBossOblivion createDefaultBoss() {
    	GlobalBossOblivion boss = new GlobalBossOblivion();

        boss.setName("OBLIVION");
        boss.setMaxHp(80_000L);
        boss.setCurrentHp(80_000L);
        boss.setProcessingDeath(false);
        boss.setAlive(true);

        //boss.setAttackPower(160L);
        //boss.setAttackIntervalSeconds(200L);

        boss.setImageUrl("images/boss_oblivion.webp");

        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(7200L); 
        boss.setSpawnCount(1);

        boss.setRewardBoss(75_000L);
        boss.setRewardExp(3000L);

        return repo.save(boss);
    }

    public GlobalBossOblivion save(GlobalBossOblivion boss) {
        return repo.save(boss);
    }

    // Ataque direto (usado em auto-attack ou debug)
    public GlobalBossOblivion attack(long damage) {
    	GlobalBossOblivion boss = get();
        boss.applyDamage(damage);
        return repo.save(boss);
    }
    
    //===========================================================
    //incrmentar hp, toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoOblivion(GlobalBossOblivion boss) {


        Random random = new Random();
    	long min = 20;
    	long max = 170;
    	long incrementarUp = random.nextLong(min, max + 1);

    	
    	long valorHpMax =  boss.getMaxHp();
    	long valorCur = boss.getCurrentHp();
    	
    	boss.setMaxHp( valorHpMax + incrementarUp);
    	boss.setCurrentHp( valorCur + incrementarUp);
    	
    	long valorXp =  boss.getRewardExp();
    	boss.setRewardExp(valorXp + 5);
    	
    	long valorsetRewardBoss = boss.getRewardBoss();
    	boss.setRewardBoss(valorsetRewardBoss + 3);
 	   
    	 //ataque respaw
        boss.setAttackPower(boss.getAttackPower() + 2);
        boss.setAttackIntervalSeconds(boss.getAttackIntervalSeconds() + 1);
    }//--->incrmentar hp, toda vez que o boss for derrotado
}
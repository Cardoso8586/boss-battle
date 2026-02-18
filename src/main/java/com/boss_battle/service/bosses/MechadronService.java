package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossMechadron;
import com.boss_battle.repository.MechadronRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MechadronService {

    @Autowired
    private MechadronRepository repo;

    /**
     * Retorna o boss com ID fixo 1
     */
    public GlobalBossMechadron get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    /**
     * Cria o boss caso não exista
     */
    public GlobalBossMechadron createDefaultBoss() {
        GlobalBossMechadron boss = new GlobalBossMechadron();

        boss.setName("MECHADRON");
        boss.setMaxHp(220_000L);
        boss.setCurrentHp(220_000L);
        boss.setProcessingDeath(false);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_mechadron.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(5400L);
        boss.setSpawnCount(1);
        boss.setAttackPower(340L);
        boss.setAttackIntervalSeconds(238L);
        boss.setRewardBoss(110_000L);
        boss.setRewardExp(2800L);

        return repo.save(boss);
    }

    /**
     * Salva alterações
     */
    public GlobalBossMechadron save(GlobalBossMechadron boss) {
        return repo.save(boss);
    }

    /**
     * Ataca o boss
     */
    public GlobalBossMechadron attack(long damage) {
        GlobalBossMechadron boss = repo.findById(1L)
            .orElseThrow(() -> new RuntimeException("Boss MECHADRON não encontrado"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }
    
    //===========================================================
    //incrmentar hp, toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoMechadron(GlobalBossMechadron boss) {


        Random random = new Random();
    	long min = 100;
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

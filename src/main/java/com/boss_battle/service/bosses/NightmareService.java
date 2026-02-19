package com.boss_battle.service.bosses;


import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossNightmare;
import com.boss_battle.repository.NightmareRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NightmareService {

    @Autowired
    private NightmareRepository repo;

    /**
     * Retorna o boss com ID fixo 1
     */
    public GlobalBossNightmare get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    /**
     * Cria o boss caso não exista na base
     */
    public GlobalBossNightmare createDefaultBoss() {
    	GlobalBossNightmare boss = new GlobalBossNightmare();

        boss.setName("NIGHTMARE");
        boss.setMaxHp(250_000);
        boss.setCurrentHp(250_000);
        boss.setProcessingDeath(false);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_nightmare.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(10800L); // 1 hora
        boss.setSpawnCount(1);
       // boss.setAttackPower(240L);
        //boss.setAttackIntervalSeconds(180L);
        boss.setRewardBoss(150_000);
        boss.setRewardExp(3000);

        return repo.save(boss);
    }

    /**
     * Apenas salva alterações
     */
    public GlobalBossNightmare save(GlobalBossNightmare boss) {
        return repo.save(boss);
    }

    /**
     * Ataca o boss e salva os dados atualizados
     */
    public GlobalBossNightmare attack(long damage) {
    	GlobalBossNightmare boss = repo.findById(1L)
            .orElseThrow(() -> new RuntimeException("Boss Azurion não encontrado"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }
    
    
  //===========================================================
    //incrmentar hp, toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoNightmare(GlobalBossNightmare boss) {


        Random random = new Random();
    	long min = 110;
    	long max = 300;
    	long incrementarUp = random.nextLong(min, max + 1);

    	
    	long valorHpMax =  boss.getMaxHp();
    	long valorCur = boss.getCurrentHp();
    	
    	boss.setMaxHp( valorHpMax + incrementarUp);
    	boss.setCurrentHp( valorCur + incrementarUp);
    	
    	long valorXp =  boss.getRewardExp();
    	boss.setRewardExp(valorXp + 5);
    	
    	long valorsetRewardBoss = boss.getRewardBoss();
    	boss.setRewardBoss(valorsetRewardBoss + 5);
    	
    	//ataque respaw
     	long valorAtaque = boss.getAttackPower();
     	boss.setAttackPower(valorAtaque+ 4);
    	long valorIntervalSeconds= boss.getAttackIntervalSeconds();
        boss.setAttackIntervalSeconds(valorIntervalSeconds + 1);
 	   
    }//--->incrmentar hp, toda vez que o boss for derrotado
}

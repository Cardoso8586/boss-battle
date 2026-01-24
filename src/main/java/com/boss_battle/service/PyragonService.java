package com.boss_battle.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossPyragon;
import com.boss_battle.repository.PyragonRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PyragonService {

    @Autowired
    private PyragonRepository repo;

    /**
     * Retorna o boss com ID fixo 1
     */
    public GlobalBossPyragon get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    /**
     * Cria o boss caso não exista na base
     */
    public GlobalBossPyragon createDefaultBoss() {
        GlobalBossPyragon boss = new GlobalBossPyragon();

        boss.setName("PYRAGON");
        boss.setMaxHp(150_000L);
        boss.setCurrentHp(150_000L);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_pyragon.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(3600L); // 1 hora
        boss.setSpawnCount(1);
        boss.setRewardBoss(75_000L);
        boss.setRewardExp(1800L);

        return repo.save(boss);
    }

    /**
     * Apenas salva alterações
     */
    public GlobalBossPyragon save(GlobalBossPyragon boss) {
        return repo.save(boss);
    }

    /**
     * Ataca o boss e salva os dados atualizados
     */
    public GlobalBossPyragon attack(long damage) {
        GlobalBossPyragon boss = repo.findById(1L)
            .orElseThrow(() -> new RuntimeException("Boss Pyragon não encontrado"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }
    
    //===========================================================
    //incrmentar hp, toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoPyragon(GlobalBossPyragon boss) {


        Random random = new Random();
    	long min = 100;
    	long max = 270;
    	long incrementarUp = random.nextLong(min, max + 1);

    	
    	long valorHpMax =  boss.getMaxHp();
    	long valorCur = boss.getCurrentHp();
    	
    	boss.setMaxHp( valorHpMax + incrementarUp);
    	boss.setCurrentHp( valorCur + incrementarUp);
    	
    	long valorXp =  boss.getRewardExp();
    	boss.setRewardExp(valorXp + 7);
    	
    	long valorsetRewardBoss = boss.getRewardBoss();
    	boss.setRewardBoss(valorsetRewardBoss + 4);
 	   
    }//--->incrmentar hp, toda vez que o boss for derrotado
}

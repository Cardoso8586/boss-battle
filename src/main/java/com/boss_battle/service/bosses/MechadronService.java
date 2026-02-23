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

	private static final long MAX_ATTACK = 2700;
	private static final long MAX_INTERVAL = 1000;
	private static final long MAX_REWARD_BOSS = 1_100_000;
	private static final long MAX_EXP = 18000;
	private static final long MAX_HP = 11_000_000;
	
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
        //boss.setAttackPower(340L);
       // boss.setAttackIntervalSeconds(238L);
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
        	long min = 10;
        	long max = 100;
        	long incrementarUp = random.nextLong(min, max + 1);
        	long valorHpMax =  boss.getMaxHp();
        	long valorCur = boss.getCurrentHp();
        	long valorAtaque = boss.getAttackPower();
            long valorIntervalSeconds = boss.getAttackIntervalSeconds();
            long valorsetRewardBoss = boss.getRewardBoss();
            
            
            //Limitar hp
            if(valorHpMax < MAX_HP) {
            	boss.setMaxHp( valorHpMax + incrementarUp);
            	boss.setCurrentHp( valorCur + incrementarUp);
        	
            }else {
            	
            	boss.setMaxHp(MAX_HP);
            	boss.setCurrentHp(MAX_HP);
            }
        	
        	//---> Limitar recompensa
        	
            if(valorsetRewardBoss < MAX_REWARD_BOSS) {
            	
            	boss.setRewardBoss(valorsetRewardBoss + 1);
            }else {
            	
            	boss.setRewardBoss(MAX_REWARD_BOSS);
            }
       
            //--->Limitar xp
     	    long valorXp =  boss.getRewardExp();
            if(valorXp < MAX_EXP) {
               boss.setRewardExp(valorXp + 1);
            }else {
            	 boss.setRewardExp(MAX_EXP);
            	
            }
            
            // Limitar Evolução do ataque
            if (valorAtaque < MAX_ATTACK) {
                boss.setAttackPower(valorAtaque + 3);
            } else {
                boss.setAttackPower(MAX_ATTACK);
            }

            // Limitar Evolução do intervalo
            if (valorIntervalSeconds < MAX_INTERVAL) {
                boss.setAttackIntervalSeconds(valorIntervalSeconds + 1);
            } else {
                boss.setAttackIntervalSeconds(MAX_INTERVAL);
            }

    }
}

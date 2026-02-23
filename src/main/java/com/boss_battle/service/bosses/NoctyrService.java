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

	private static final long MAX_ATTACK = 2700;
	private static final long MAX_INTERVAL = 1100;
	private static final long MAX_REWARD_BOSS = 1_100_000;
	private static final long MAX_EXP = 18000;
	private static final long MAX_HP = 11_000_000;
	
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
        //boss.setAttackPower(160L);
       // boss.setAttackIntervalSeconds(185L);
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
                boss.setAttackPower(valorAtaque + 5);
            } else {
                boss.setAttackPower(MAX_ATTACK);
            }

            // Limitar Evolução do intervalo
            if (valorIntervalSeconds < MAX_INTERVAL) {
                boss.setAttackIntervalSeconds(valorIntervalSeconds + 1);
            } else {
                boss.setAttackIntervalSeconds(MAX_INTERVAL);
            }

          
          
    }//--->incrmentar hp, toda vez que o boss for derrotado
}
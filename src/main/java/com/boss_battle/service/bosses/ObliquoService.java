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

	private static final long MAX_ATTACK = 1500;
	private static final long MAX_INTERVAL = 800;
	private static final long MAX_REWARD_BOSS = 1_200_000;
	private static final long MAX_EXP = 23000;
	private static final long MAX_HP = 13_000_000;
	
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

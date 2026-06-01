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
	
	private static final long MAX_ATTACK = 1_900;
	private static final long MAX_INTERVAL = 1_200;
	private static final long MAX_REWARD_BOSS = 500_000;
	private static final long MAX_EXP = 70_000;
	private static final long MAX_HP = 900_000;

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
        //boss.setMaxHp(80_000L);
       // boss.setCurrentHp(80_000L);
        boss.setProcessingDeath(false);
        boss.setAlive(true);

        //boss.setAttackPower(160L);
        //boss.setAttackIntervalSeconds(200L);

        boss.setImageUrl("images/boss_oblivion.webp");

        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(7200L); 
        boss.setSpawnCount(1);

       // boss.setRewardBoss(75_000L);
        //boss.setRewardExp(3000L);

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
        	long min = 220;
        	long max = 300;
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
            	
            	boss.setRewardBoss(valorsetRewardBoss + 31);
            }else {
            	
            	boss.setRewardBoss(MAX_REWARD_BOSS);
            }
       
            //--->Limitar xp
     	    long valorXp =  boss.getRewardExp();
            if(valorXp < MAX_EXP) {
               boss.setRewardExp(valorXp + 31);
            }else {
            	 boss.setRewardExp(MAX_EXP);
            	
            }
            
            // Limitar Evolução do ataque
            if (valorAtaque < MAX_ATTACK) {
                boss.setAttackPower(valorAtaque + 35);
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
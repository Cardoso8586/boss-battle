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

	private static final long MAX_ATTACK = 1700;
	private static final long MAX_INTERVAL = 1000;
	private static final long MAX_REWARD_BOSS = 1_100_000;
	private static final long MAX_EXP = 18000;
	private static final long MAX_HP = 11_000_000;
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
        //boss.setAttackPower(320L);
        //boss.setAttackIntervalSeconds(230L);
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

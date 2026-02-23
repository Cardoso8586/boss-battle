package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossAzraelPrime;
import com.boss_battle.repository.AzraelPrimeRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AzraelPrimeService {

	private static final long MAX_ATTACK = 1200;
	private static final long MAX_INTERVAL = 1100;
	private static final long MAX_REWARD_BOSS = 1_000_000;
	private static final long MAX_EXP = 15000;
	private static final long MAX_HP = 15_000_000;
	
    @Autowired
    private AzraelPrimeRepository repo;

    // Sempre retorna o boss único (ID = 1)
    public GlobalBossAzraelPrime get() {
        return repo.findById(1L).orElseGet(this::createDefaultBoss);
    }

    // Cria o boss padrão
    public GlobalBossAzraelPrime createDefaultBoss() {
    	GlobalBossAzraelPrime boss = new GlobalBossAzraelPrime();

        boss.setName("AZRAEL PRIME");
        aplicarEscalamentoAzraelPrime(boss);
        boss.setProcessingDeath(false);
        boss.setAlive(true);

        boss.setImageUrl("images/boss_azrael_prime.webp");

        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(14_400L); 
        boss.setSpawnCount(1);
        boss.setRewardBoss(150_000L);
        boss.setRewardExp(10_000L);

        return repo.save(boss);
    }

    public GlobalBossAzraelPrime save(GlobalBossAzraelPrime boss) {
        return repo.save(boss);
    }

    // Ataque direto (usado em auto-attack ou debug)
    public GlobalBossAzraelPrime attack(long damage) {
    	GlobalBossAzraelPrime boss = get();
        boss.applyDamage(damage);
        return repo.save(boss);
    }
    
    //===========================================================
    //incrmentar hp, toda vez que o boss morrer
    //===========================================================
    
    public void aplicarEscalamentoAzraelPrime (GlobalBossAzraelPrime boss) {



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
            boss.setAttackPower(valorAtaque + 2);
        } else {
            boss.setAttackPower(MAX_ATTACK);
        }

        // Limitar Evolução do intervalo
        if (valorIntervalSeconds < MAX_INTERVAL) {
            boss.setAttackIntervalSeconds(valorIntervalSeconds + 1);
        } else {
            boss.setAttackIntervalSeconds(MAX_INTERVAL);
        }

 	   
    }//--->incrmentar hp, toda vez que o boss morrer
}
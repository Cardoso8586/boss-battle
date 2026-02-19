package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossMalphion;
import com.boss_battle.repository.MalphionRepository;

import jakarta.transaction.Transactional;



@Service
@Transactional
public class MalphionService  {
	
    @Autowired
    private MalphionRepository repo;

    // Sempre retorna o boss único (ID = 1)
    public GlobalBossMalphion get() {
    	  return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    // Cria o boss padrão
    public GlobalBossMalphion createDefaultBoss() {
    	GlobalBossMalphion boss = new GlobalBossMalphion();

        boss.setName("DESTRUIDOR");
        boss.setMaxHp(380_000L);
        boss.setCurrentHp(380_000L);
        boss.setProcessingDeath(false);
        boss.setAlive(true);
       // boss.setAttackPower(200L);
       // boss.setAttackIntervalSeconds(190L);
        boss.setImageUrl("images/boss_malphion.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(15_000L); 
        boss.setSpawnCount(1);
        
        boss.setRewardBoss(140_000L);
        boss.setRewardExp(9_000L);

        return repo.save(boss);
    }

    public GlobalBossMalphion save(GlobalBossMalphion boss) {
        return repo.save(boss);
    }
    
    //===========================================================
    //incrmentar hp, toda vez que o boss morrer/ se der certo..
    //===========================================================
    public void aplicarEscalamentoMalphion(GlobalBossMalphion boss) {


        Random random = new Random();
    	long min = 10;
    	long max = 100;
    	long incrementarUp = random.nextLong(min, max + 1);
    	
    	long valorHpMax =  boss.getMaxHp();
    	long valorCur = boss.getCurrentHp();
    	
    	//hp
    	boss.setMaxHp( valorHpMax + incrementarUp);
    	boss.setCurrentHp( valorCur + incrementarUp);
    	
    	//recompensa
    	long valorXp =  boss.getRewardExp();
    	boss.setRewardExp(valorXp + 1);
    	long valorsetRewardBoss = boss.getRewardBoss();
 	    boss.setRewardBoss(valorsetRewardBoss + 1);
 	   
 	   //ataque 
        boss.setAttackPower(boss.getAttackPower() + 5);
        boss.setAttackIntervalSeconds(boss.getAttackIntervalSeconds() + 1);
        
        
   	    //ataque respaw
     	long valorAtaque = boss.getAttackPower();
     	boss.setAttackPower(valorAtaque+ 6);
    	long valorIntervalSeconds= boss.getAttackIntervalSeconds();
        boss.setAttackIntervalSeconds(valorIntervalSeconds + 1);
        
        evoluirMalphion(boss);
 	   
    }
    
    // ================== EVOLUÇÃO ==================
    public void evoluirMalphion(GlobalBossMalphion boss) {

        if (boss.getMaxHp() < 400_000L) {

        	boss.setName("MALPHION");
        	boss.setImageUrl("images/boss_evolution/boss_malphion/boss_malphion.webp");

        } else if (boss.getMaxHp() < 600_000L) {

        	boss.setName("MALPHION OVERLORD");
        	boss.setImageUrl("images/boss_evolution/boss_malphion/boss_malphion_overlord.webp");

        } else if (boss.getMaxHp() < 800_000L) {

        	boss.setName( "MALPHION OMEGA");
        	boss.setImageUrl("images/boss_evolution/boss_malphion/boss_malphion_omega.webp");

        } else {

        	boss.setName("MALPHION ETERNAL");
        	boss.setImageUrl("images/boss_evolution/boss_malphion/boss_malphion_eternal.webp");
        }
    }

   

    // Ataque direto (usado em auto-attack ou debug)
    public GlobalBossMalphion attack(long damage) {
    	GlobalBossMalphion boss = get();
        boss.applyDamage(damage);
        return repo.save(boss);
    }
}


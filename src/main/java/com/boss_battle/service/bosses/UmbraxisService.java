

package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boss_battle.model.GlobalBossUmbraxis;
import com.boss_battle.repository.UmbraxisRepository;

@Service
@Transactional
public class UmbraxisService {

	private static final long MAX_ATTACK = 1700;
	private static final long MAX_INTERVAL = 600;
	private static final long MAX_REWARD_BOSS = 1_300_000;
	private static final long MAX_EXP = 25000;
	private static final long MAX_HP = 9_000_000;
	
    @Autowired
    private UmbraxisRepository repo;

    public GlobalBossUmbraxis get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossUmbraxis createDefaultBoss() {
    	GlobalBossUmbraxis boss = new GlobalBossUmbraxis();
         boss.setName("UMBRA XIS");
         aplicarEscalamentoUmbraxis(boss);
        // boss.setMaxHp(200_000);
        // boss.setCurrentHp(200_000);
         boss.setAlive(true);
         boss.setImageUrl("images/boss_umbraxis.webp");
         boss.setSpawnedAt(LocalDateTime.now());
         boss.setRespawnCooldownSeconds(7200);
         boss.setSpawnCount(1);
        // boss.setAttackIntervalSeconds(185L);
        // boss.setAttackPower(150L);
         boss.setRewardBoss(100_000);
         boss.setRewardExp(2500);
        return repo.save(boss);
    }

    public GlobalBossUmbraxis save(GlobalBossUmbraxis boss) {
        return repo.save(boss);
    }
    
    public GlobalBossUmbraxis attack(long damage) {
        var boss = repo.findById(1L)
                       .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss); // <- atualiza no banco
    }

            //===========================================================
    	    //incrmentar hp, toda vez que o boss for derrotado
    	    //===========================================================
    	    public void aplicarEscalamentoUmbraxis(GlobalBossUmbraxis boss) {


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
    	              boss.setAttackPower(valorAtaque + 4);
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

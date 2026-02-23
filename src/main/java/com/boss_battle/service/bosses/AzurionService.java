package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossAzurion;
import com.boss_battle.repository.AzurionRepository;

@Service
public class AzurionService {
	private static final long MAX_ATTACK = 3200;
	private static final long MAX_INTERVAL = 1500;
	private static final long MAX_REWARD_BOSS = 1_200_000;
	private static final long MAX_EXP = 16000;
	private static final long MAX_HP = 11_000_000;
    @Autowired
    private AzurionRepository repo;

    /**
     * Retorna o boss com ID fixo 1
     */
    public GlobalBossAzurion get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    /**
     * Cria o boss caso não exista na base
     */
    public GlobalBossAzurion createDefaultBoss() {
        GlobalBossAzurion boss = new GlobalBossAzurion();

        boss.setName("AZURION");
        boss.setMaxHp(100_000);
        boss.setCurrentHp(100_000);
        boss.setProcessingDeath(false);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_azurion.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(3600); // 1 hora
        
        boss.setSpawnCount(1);
        //boss.setAttackPower(120L);
       // boss.setAttackIntervalSeconds(160L);
        boss.setRewardBoss(35_000);
        boss.setRewardExp(1300);

        return repo.save(boss);
    }

    /**
     * Apenas salva alterações
     */
    public GlobalBossAzurion save(GlobalBossAzurion boss) {
        return repo.save(boss);
    }

    /**
     * Ataca o boss e salva os dados atualizados
     */
    public GlobalBossAzurion attack(long damage) {
        GlobalBossAzurion boss = repo.findById(1L)
            .orElseThrow(() -> new RuntimeException("Boss Azurion não encontrado"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }
    
    
    public void aplicarEscalamentoAzurion (GlobalBossAzurion boss) {


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

 	   
    }//--->incrmentar hp, toda vez que o boss morrer
}

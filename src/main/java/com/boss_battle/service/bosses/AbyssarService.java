package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossAbyssar;
import com.boss_battle.repository.AbyssarRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AbyssarService {

	private static final long MAX_ATTACK = 3000;
	private static final long MAX_INTERVAL = 1000;
	private static final long MAX_REWARD_BOSS = 1_000_000;
	private static final long MAX_EXP = 15000;
	private static final long MAX_HP = 15_000_000;

    @Autowired
    private AbyssarRepository repo;

    // Sempre retorna o boss único (ID = 1)
    public GlobalBossAbyssar get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    // Cria o boss padrão
    public GlobalBossAbyssar createDefaultBoss() {

        GlobalBossAbyssar boss = new GlobalBossAbyssar();

        boss.setName("ABYSSAR DOMINATOR");
        boss.setMaxHp(520_000L);
        boss.setCurrentHp(520_000L);
        boss.setProcessingDeath(false);
        boss.setAlive(true);

        //boss.setAttackPower(400L);
        //boss.setAttackIntervalSeconds(190L);

        boss.setImageUrl("images/boss_evolution/boss_abyssar/boss_abyssar.webp");

        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(18_000L);
        boss.setSpawnCount(1);

        boss.setRewardBoss(210_000L);
        boss.setRewardExp(14_000L);

        return repo.save(boss);
    }

    public GlobalBossAbyssar save(GlobalBossAbyssar boss) {
        return repo.save(boss);
    }

    //===========================================================
    // ESCALAMENTO AO MORRER
    //===========================================================
    public void aplicarEscalamentoAbyssar(GlobalBossAbyssar boss) {

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

        
        
        evoluirAbyssar(boss);
        
    }

 // ================== EVOLUÇÃO ==================
    public void evoluirAbyssar(GlobalBossAbyssar boss) {

        long hp = boss.getMaxHp();

        if (hp < 600_000L) {

            boss.setName("ABYSSAR DOMINADOR");
            boss.setImageUrl("images/boss_evolution/boss_abyssar/boss_abyssar.webp");

        } else if (hp < 850_000L) {

            boss.setName("ABYSSAR, TIRANO DO VAZIO");
            boss.setImageUrl("images/boss_evolution/boss_abyssar/boss_abyssar_void.webp");

        } else if (hp < 1_200_000L) {

            boss.setName("ABYSSAR, MONARCA ABISSAL");
            boss.setImageUrl("images/boss_evolution/boss_abyssar/boss_abyssar_monarch.webp");

        } else {

            boss.setName("ABYSSAR, IMPERADOR DO ABISMO ETERNO");
            boss.setImageUrl("images/boss_evolution/boss_abyssar/boss_abyssar_emperor.webp");
        }
    }



    // Ataque direto
    public GlobalBossAbyssar attack(long damage) {

        GlobalBossAbyssar boss = get();
        boss.applyDamage(damage);

        return repo.save(boss);
    }
}

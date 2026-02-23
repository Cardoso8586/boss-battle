package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossReflexa;
import com.boss_battle.repository.ReflexaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReflexaService {

	private static final long MAX_ATTACK = 2100;
	private static final long MAX_INTERVAL = 1000;
	private static final long MAX_REWARD_BOSS = 1_100_000;
	private static final long MAX_EXP = 20000;
	private static final long MAX_HP = 12_000_000;
	
    @Autowired
    private ReflexaRepository repo;

    /**
     * Retorna o boss com ID fixo 1
     */
    public GlobalBossReflexa get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    /**
     * Cria o boss caso não exista na base
     */
    public GlobalBossReflexa createDefaultBoss() {
        GlobalBossReflexa boss = new GlobalBossReflexa();

        boss.setName("REFLEXA");
        boss.setMaxHp(170_000L);
        boss.setCurrentHp(170_000L);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_reflexa.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(4800L);
        boss.setSpawnCount(1);
       // boss.setAttackIntervalSeconds(235L);
       // boss.setAttackPower(200L);
        boss.setRewardBoss(90_000L);
        boss.setRewardExp(2400L);

        return repo.save(boss);
    }

    /**
     * Apenas salva alterações
     */
    public GlobalBossReflexa save(GlobalBossReflexa boss) {
        return repo.save(boss);
    }

    /**
     * Ataca o boss e salva os dados atualizados
     */
    public GlobalBossReflexa attack(long damage) {
        GlobalBossReflexa boss = repo.findById(1L)
            .orElseThrow(() -> new RuntimeException("Boss REFLEXA não encontrado"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }
    
    //===========================================================
    //incrmentar hp, toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoReflexa(GlobalBossReflexa boss) {


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

package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossPyragon;
import com.boss_battle.repository.PyragonRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PyragonService {


	private static final long MAX_ATTACK = 2200;
	private static final long MAX_INTERVAL = 1100;
	private static final long MAX_REWARD_BOSS = 1_500_000;
	private static final long MAX_EXP = 30000;
	private static final long MAX_HP = 10_000_000;
	
    @Autowired
    private PyragonRepository repo;

    /**
     * Retorna o boss com ID fixo 1
     */
    public GlobalBossPyragon get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    /**
     * Cria o boss caso não exista na base
     */
    public GlobalBossPyragon createDefaultBoss() {
        GlobalBossPyragon boss = new GlobalBossPyragon();

        boss.setName("PYRAGON");
        boss.setMaxHp(150_000L);
        boss.setCurrentHp(150_000L);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_pyragon.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(3600L); // 1 hora
        boss.setSpawnCount(1);
        //boss.setAttackIntervalSeconds(210L);
       // boss.setAttackPower(220L);
        boss.setRewardBoss(75_000L);
        boss.setRewardExp(1800L);

        return repo.save(boss);
    }

    /**
     * Apenas salva alterações
     */
    public GlobalBossPyragon save(GlobalBossPyragon boss) {
        return repo.save(boss);
    }

    /**
     * Ataca o boss e salva os dados atualizados
     */
    public GlobalBossPyragon attack(long damage) {
        GlobalBossPyragon boss = repo.findById(1L)
            .orElseThrow(() -> new RuntimeException("Boss Pyragon não encontrado"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }
    
    //===========================================================
    //incrmentar hp, toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoPyragon(GlobalBossPyragon boss) {


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

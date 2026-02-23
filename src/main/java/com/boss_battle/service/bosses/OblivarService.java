package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossOblivar;
import com.boss_battle.repository.OblivarRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OblivarService {

	private static final long MAX_ATTACK = 1800;
	private static final long MAX_INTERVAL = 1100;
	private static final long MAX_REWARD_BOSS = 1_200_000;
	private static final long MAX_EXP = 25000;
	private static final long MAX_HP = 15_000_000;
    @Autowired
    private OblivarRepository repo;

    /**
     * Retorna o boss com ID fixo 1
     */
    public GlobalBossOblivar get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    /**
     * Cria o boss caso não exista na base
     */
    public GlobalBossOblivar createDefaultBoss() {
    	GlobalBossOblivar boss = new GlobalBossOblivar();

        boss.setName("OBLIVAR");
        boss.setMaxHp(35_000);
        boss.setCurrentHp(35_000);
        boss.setProcessingDeath(false);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_oblivar.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(4200L); // 1 hora
        boss.setSpawnCount(1);
        //boss.setAttackIntervalSeconds(148L);
        //boss.setAttackPower(120L);
        boss.setRewardBoss(30_000);
        boss.setRewardExp(1650);

        return repo.save(boss);
    }

    /**
     * Apenas salva alterações
     */
    public GlobalBossOblivar save(GlobalBossOblivar boss) {
        return repo.save(boss);
    }

    /**
     * Ataca o boss e salva os dados atualizados
     */
    public GlobalBossOblivar attack(long damage) {
    	GlobalBossOblivar boss = repo.findById(1L)
            .orElseThrow(() -> new RuntimeException("Boss Azurion não encontrado"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }
    
    //===========================================================
    //incrmentar hp, toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoOblivar(GlobalBossOblivar boss) {

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
package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossLyxara;
import com.boss_battle.repository.LyxaraRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LyxaraService {

    @Autowired
    private LyxaraRepository repo;

    // Sempre retorna o boss único (ID = 1)
    public GlobalBossLyxara get() {
        return repo.findById(1L).orElseGet(this::createDefaultBoss);
    }

    // Cria o boss padrão
    public GlobalBossLyxara createDefaultBoss() {
        GlobalBossLyxara boss = new GlobalBossLyxara();

        boss.setName("LYXARA — A Soberana Sombria");
        boss.setMaxHp(40_000L);
        boss.setCurrentHp(40_000L);
        boss.setProcessingDeath(false);
        boss.setAlive(true);

       // boss.setAttackPower(140L);
        //boss.setAttackIntervalSeconds(142L);

        boss.setImageUrl("images/boss_lyxara.webp");

        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(4800L); // 80 minutos
        boss.setSpawnCount(1);

        boss.setRewardBoss(40_000L);
        boss.setRewardExp(3000L);

        return repo.save(boss);
    }

    public GlobalBossLyxara save(GlobalBossLyxara boss) {
        return repo.save(boss);
    }

    // Ataque direto (usado em auto-attack ou debug)
    public GlobalBossLyxara attack(long damage) {
        GlobalBossLyxara boss = get();
        boss.applyDamage(damage);
        return repo.save(boss);
    }
    
    //===========================================================
    //incrmentar hp, toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoLyxara(GlobalBossLyxara boss) {


        Random random = new Random();
    	long min = 100;
    	long max = 200;
    	long incrementarUp = random.nextLong(min, max + 1);

    	
    	long valorHpMax =  boss.getMaxHp();
    	long valorCur = boss.getCurrentHp();
    	
    	boss.setMaxHp( valorHpMax + incrementarUp);
    	boss.setCurrentHp( valorCur + incrementarUp);
    	
    	long valorXp =  boss.getRewardExp();
    	boss.setRewardExp(valorXp + 3);
    	
    	long valorsetRewardBoss = boss.getRewardBoss();
    	boss.setRewardBoss(valorsetRewardBoss + 1);
    	
    	 //ataque respaw
        boss.setAttackPower(boss.getAttackPower() + 2);
        boss.setAttackIntervalSeconds(boss.getAttackIntervalSeconds() + 1);
 	   
    }//--->incrmentar hp, toda vez que o boss for derrotado
   
}

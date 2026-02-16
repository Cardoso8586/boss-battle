package com.boss_battle.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "global_boss_thunderon")
public class GlobalBossThunderon implements BattleBoss {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name = "THUNDERON";

    @Column(nullable = false)
    private long maxHp = 260_000L;

    @Column(nullable = false)
    private long currentHp = 260_000L;

    private long attackPower = 160L;

    private long attackIntervalSeconds = 220L;

    @Column(columnDefinition = "DATETIME")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime spawnedAt;

    private boolean alive = true;

    @Column(columnDefinition = "DATETIME")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime respawnAt;

    private long respawnCooldownSeconds = 6000L;

    private int spawnCount = 0;

    @Column(nullable = true)
    private String imageUrl = "images/boss_thunderon.webp";

    @Column(nullable = false)
    private long rewardBoss = 140_000L;

    @Column(nullable = false)
    private long rewardExp = 3600L;
    
    @Column(nullable = false)
    private boolean processingDeath = false;

    @Override
    public boolean isProcessingDeath() {
        return processingDeath;
    }

    @Override
    public void setProcessingDeath(boolean processingDeath) {
        this.processingDeath = processingDeath;
    }

    // ✅ CAMPO PERSISTIDO
    @Column(nullable = false)
    private boolean rewardDistributed = false;
  
    // getters / setters
    
    @Column(columnDefinition = "DATETIME")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastAttackAt;

    public LocalDateTime getLastAttackAt() { return lastAttackAt; }
    public void setLastAttackAt(LocalDateTime lastAttackAt) {
        this.lastAttackAt = lastAttackAt;
    }
    public boolean isRewardDistributed() {
        return rewardDistributed;
    }

    public void setRewardDistributed(boolean rewardDistributed) {
        this.rewardDistributed = rewardDistributed;
    }
 

    public GlobalBossThunderon() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public long getMaxHp() { return maxHp; }
    public void setMaxHp(long maxHp) { this.maxHp = maxHp; }

    public long getCurrentHp() { return currentHp; }
    public void setCurrentHp(long currentHp) { this.currentHp = currentHp; }

    public long getAttackPower() { return attackPower; }
    public void setAttackPower(long attackPower) { this.attackPower = attackPower; }

    public long getAttackIntervalSeconds() { return attackIntervalSeconds; }
    public void setAttackIntervalSeconds(long attackIntervalSeconds) { this.attackIntervalSeconds = attackIntervalSeconds; }

    public LocalDateTime getSpawnedAt() { return spawnedAt; }
    public void setSpawnedAt(LocalDateTime spawnedAt) { this.spawnedAt = spawnedAt; }

    public boolean isAlive() { return alive; }
    public void setAlive(boolean alive) { this.alive = alive; }

    public LocalDateTime getRespawnAt() { return respawnAt; }
    public void setRespawnAt(LocalDateTime respawnAt) { this.respawnAt = respawnAt; }

    public long getRespawnCooldownSeconds() { return respawnCooldownSeconds; }
    public void setRespawnCooldownSeconds(long respawnCooldownSeconds) { this.respawnCooldownSeconds = respawnCooldownSeconds; }

    public long getSpawnCount() { return spawnCount; }
    public void setSpawnCount(int spawnCount) { this.spawnCount = spawnCount; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public long getRewardBoss() { return rewardBoss; }
    public void setRewardBoss(long rewardBoss) { this.rewardBoss = rewardBoss; }

    public long getRewardExp() { return rewardExp; }
    public void setRewardExp(long rewardExp) { this.rewardExp = rewardExp; }

    @Override
    public String getBossName() {
        return this.name;
    }

    @Override
    public void setSpawnCount(long value) {
        this.spawnCount = Math.toIntExact(value);
    }

    @Override
    public Map<String, Long> applyDamage(long damage) {
        Map<String, Long> reward = new HashMap<>();

        if (!this.alive) {
            reward.put("bossReward", 0L);
            reward.put("expReward", 0L);
            return reward;
        }

        this.currentHp -= damage;
        if (this.currentHp < 0) this.currentHp = 0;

        if (this.currentHp == 0) {
            this.alive = false;
            this.respawnAt = LocalDateTime.now().plusSeconds(respawnCooldownSeconds);

            reward.put("bossReward", this.rewardBoss);
            reward.put("expReward", this.rewardExp);
        } else {
            reward.put("bossReward", 0L);
            reward.put("expReward", 0L);
        }

        return reward;
    }
    
    
    
    //===========================================================
    //incrmentar hp, toda vez que o boss for derrotado
    //===========================================================
    
    public void aplicarEscalamentoThunderon() {


        Random random = new Random();
    	long min = 50;
    	long max = 100;
    	long incrementarUp = random.nextLong(min, max + 1);

    	
    	long valorHpMax =  getMaxHp();
    	long valorCur = getCurrentHp();
    	
    	setMaxHp( valorHpMax + incrementarUp);
    	setCurrentHp( valorCur + incrementarUp);
    	
    	long valorXp =  getRewardExp();
    	setRewardExp(valorXp + 5);
    	
    	long valorsetRewardBoss = getRewardBoss();
 	    setRewardBoss(valorsetRewardBoss + 2);
 	   
    }//--->incrmentar hp, toda vez que o boss for derrotado
}

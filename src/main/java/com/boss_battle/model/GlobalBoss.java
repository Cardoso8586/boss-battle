package com.boss_battle.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "global_boss")
public class GlobalBoss implements BattleBoss {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private long maxHp;

    @Column(nullable = false)
    private long currentHp;

    private long attackPower;

    private long attackIntervalSeconds;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime spawnedAt;

    private boolean alive;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime respawnAt;

    private long respawnCooldownSeconds;

    private int spawnCount;

    private String imageUrl;
    
    @Column(nullable = false)
    private long rewardBoss; // moedas

    @Column(nullable = false)
    private long rewardExp; // exp

    @Column(nullable = false)
    private boolean processingDeath = false;

    @Column(columnDefinition = "DATETIME")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastAttackAt;

    public LocalDateTime getLastAttackAt() { return lastAttackAt; }
    public void setLastAttackAt(LocalDateTime lastAttackAt) {
        this.lastAttackAt = lastAttackAt;
    }
    @Override
    public boolean isProcessingDeath() {
        return processingDeath;
    }

    @Override
    public void setProcessingDeath(boolean processingDeath) {
        this.processingDeath = processingDeath;
    }

    
    // construtor vazio
    public GlobalBoss() {}
    
    // ✅ CAMPO PERSISTIDO
    @Column(nullable = false)
    private boolean rewardDistributed = false;
 
    // getters / setters
    public boolean isRewardDistributed() {
        return rewardDistributed;
    }

    public void setRewardDistributed(boolean rewardDistributed) {
        this.rewardDistributed = rewardDistributed;
    }
 

 
  

    // --------------------------------
    // IMPLEMENTAÇÃO DE BattleBoss
    // --------------------------------

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getBossName() {
        return name;
    }

    @Override
    public long getMaxHp() {
        return maxHp;
    }

    @Override
    public long getCurrentHp() {
        return currentHp;
    }

    @Override
    public void setCurrentHp(long hp) {
        this.currentHp = hp;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    @Override
    public long getRespawnCooldownSeconds() {
        return respawnCooldownSeconds;
    }

    @Override
    public void setRespawnAt(LocalDateTime time) {
        this.respawnAt = time;
    }

    @Override
    public LocalDateTime getRespawnAt() {
        return respawnAt;
    }

    @Override
    public long getSpawnCount() {
        return spawnCount;
    }

    @Override
    public void setSpawnCount(long value) {
        this.spawnCount = (int) value;
    }

    // --------------------------------
    // GETTERS E SETTERS NORMAIS
    // --------------------------------
    
    public long getRewardBoss() {
        return rewardBoss;
    }

    public void setRewardBoss(long rewardBoss) {
        this.rewardBoss = rewardBoss;
    }

    public long getRewardExp() {
        return rewardExp;
    }

    public void setRewardExp(long rewardExp) {
        this.rewardExp = rewardExp;
    }
    


    public void setId(Long id) {
        this.id = id;
    }

    public void setMaxHp(long maxHp) {
        this.maxHp = maxHp;
    }

    public long getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(long attackPower) {
        this.attackPower = attackPower;
    }

    public long getAttackIntervalSeconds() {
        return attackIntervalSeconds;
    }

    public void setAttackIntervalSeconds(long attackIntervalSeconds) {
        this.attackIntervalSeconds = attackIntervalSeconds;
    }

    public LocalDateTime getSpawnedAt() {
        return spawnedAt;
    }

    public void setSpawnedAt(LocalDateTime spawnedAt) {
        this.spawnedAt = spawnedAt;
    }

    public void setRespawnCooldownSeconds(long respawnCooldownSeconds) {
        this.respawnCooldownSeconds = respawnCooldownSeconds;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
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

	
}

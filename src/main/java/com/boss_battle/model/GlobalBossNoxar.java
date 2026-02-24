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
@Table(name = "global_boss_noxar")
public class GlobalBossNoxar implements BattleBoss {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name = "NOXAR"; // nome sombrio e chamativo

    @Column(nullable = false)
    private long maxHp = 50_000L;

    @Column(nullable = false)
    private long currentHp = 50_000L;

    private long attackPower = 80L;

    private long attackIntervalSeconds = 140L;

    @Column(columnDefinition = "DATETIME")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime spawnedAt;

    private boolean alive = true;

    @Column(columnDefinition = "DATETIME")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime respawnAt;

    private long respawnCooldownSeconds = 5400L; // 90 min

    private int spawnCount = 0;

    @Column(nullable = true)
    private String imageUrl = "images/boss_noxar.webp";

    @Column(nullable = false)
    private long rewardBoss = 50_000L;

    @Column(nullable = false)
    private long rewardExp = 2200;
    
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
    public boolean isRewardDistributed() {
        return rewardDistributed;
    }

    public void setRewardDistributed(boolean rewardDistributed) {
        this.rewardDistributed = rewardDistributed;
    }
 
    @Column(columnDefinition = "DATETIME")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastAttackAt;

    public LocalDateTime getLastAttackAt() { return lastAttackAt; }
    public void setLastAttackAt(LocalDateTime lastAttackAt) {
        this.lastAttackAt = lastAttackAt;
    }
    
    public GlobalBossNoxar() {}

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
    public String getBossName() { return this.name; }

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

        long finalHp = this.currentHp - damage;
        if (finalHp < 0) finalHp = 0;

        this.currentHp = finalHp;

        if (finalHp == 0) {
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

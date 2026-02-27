package com.boss_battle.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;

@Entity
@Table(name = "global_boss_abissal")
public class GlobalBossAbissal implements BattleBoss {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name = "SOBERANO ABISSAL";

    @Column(nullable = false)
    private long maxHp = 100_000L;

    @Column(nullable = false)
    private long currentHp = 100_000L;

    private long attackPower = 90L;

    private long attackIntervalSeconds = 100L;

    @Column(columnDefinition = "DATETIME")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime spawnedAt;

    private boolean alive = true;

    @Column(columnDefinition = "DATETIME")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime respawnAt;

    private long respawnCooldownSeconds = 18_000L; // 5h

    private int spawnCount = 0;

    @Column(nullable = true)
    private String imageUrl = "images/boss_abissal.webp";

    @Column(nullable = false)
    private long rewardBoss = 100_000L;

    @Column(nullable = false)
    private long rewardExp = 10_000L;

    @Column(nullable = false)
    private boolean processingDeath = false;

    @Column(nullable = false)
    private boolean rewardDistributed = false;

    @Column(columnDefinition = "DATETIME")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastAttackAt;

    public GlobalBossAbissal() {}

    // ================= GETTERS & SETTERS =================

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
    public void setAttackIntervalSeconds(long value) {
        this.attackIntervalSeconds = value;
    }

    public LocalDateTime getSpawnedAt() { return spawnedAt; }
    public void setSpawnedAt(LocalDateTime spawnedAt) { this.spawnedAt = spawnedAt; }

    public boolean isAlive() { return alive; }
    public void setAlive(boolean alive) { this.alive = alive; }

    public LocalDateTime getRespawnAt() { return respawnAt; }
    public void setRespawnAt(LocalDateTime respawnAt) { this.respawnAt = respawnAt; }

    public long getRespawnCooldownSeconds() { return respawnCooldownSeconds; }
    public void setRespawnCooldownSeconds(long value) {
        this.respawnCooldownSeconds = value;
    }

    public long getSpawnCount() { return spawnCount; }

    @Override
    public void setSpawnCount(long value) {
        this.spawnCount = Math.toIntExact(value);
    }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public long getRewardBoss() { return rewardBoss; }
    public void setRewardBoss(long rewardBoss) { this.rewardBoss = rewardBoss; }

    public long getRewardExp() { return rewardExp; }
    public void setRewardExp(long rewardExp) { this.rewardExp = rewardExp; }

    public boolean isProcessingDeath() { return processingDeath; }
    public void setProcessingDeath(boolean processingDeath) {
        this.processingDeath = processingDeath;
    }

    public boolean isRewardDistributed() { return rewardDistributed; }
    public void setRewardDistributed(boolean rewardDistributed) {
        this.rewardDistributed = rewardDistributed;
    }

    public LocalDateTime getLastAttackAt() { return lastAttackAt; }
    public void setLastAttackAt(LocalDateTime lastAttackAt) {
        this.lastAttackAt = lastAttackAt;
    }

    // ================= BattleBoss =================

    @Override
    public String getBossName() {
        return name;
    }

    @Override
    public Map<String, Long> applyDamage(long damage) {

        Map<String, Long> reward = new HashMap<>();

        if (!alive) {
            reward.put("bossReward", 0L);
            reward.put("expReward", 0L);
            return reward;
        }

        long finalHp = currentHp - damage;
        if (finalHp < 0) finalHp = 0;

        currentHp = finalHp;

        if (finalHp == 0) {
            alive = false;
            respawnAt = LocalDateTime.now()
                    .plusSeconds(respawnCooldownSeconds);

            reward.put("bossReward", rewardBoss);
            reward.put("expReward", rewardExp);
        } else {
            reward.put("bossReward", 0L);
            reward.put("expReward", 0L);
        }

        return reward;
    }
}
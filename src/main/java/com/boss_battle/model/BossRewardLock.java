package com.boss_battle.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;

@Entity
@Table(
  name = "boss_reward_lock",
  uniqueConstraints = @UniqueConstraint(columnNames = "bossName")
)
public class BossRewardLock {

    @Id
    @Column(name = "boss_name", nullable = false)
    private String bossName;

    @Column(nullable = false)
    private boolean rewardDistributed = false;

    @Version
    @Column(nullable = false)
    private Long version = 0L;

    // 🔹 GETTERS / SETTERS

    public String getBossName() {
        return bossName;
    }

    public void setBossName(String bossName) {
        this.bossName = bossName;
    }

    public boolean isRewardDistributed() {
        return rewardDistributed;
    }

    public void setRewardDistributed(boolean rewardDistributed) {
        this.rewardDistributed = rewardDistributed;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}


package com.boss_battle.model;

import java.time.LocalDateTime;
import java.util.Map;

public interface BattleBoss {
    Long getId();
    String getBossName();

    long getMaxHp();
    long getCurrentHp();
    void setCurrentHp(long hp);

    boolean isAlive();
    void setAlive(boolean alive);

    long getRespawnCooldownSeconds();
    void setRespawnAt(LocalDateTime time);
    LocalDateTime getRespawnAt();

    long getSpawnCount();
    void setSpawnCount(long value);
    
    // 💰 RECOMPENSAS (OBRIGATÓRIO)
    long getRewardBoss();   // dinheiro
    long getRewardExp();    // experiência
    public String getImageUrl();
	Map<String, Long> applyDamage(long damage);
	
  
}

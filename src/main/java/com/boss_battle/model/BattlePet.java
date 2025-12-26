package com.boss_battle.model;

import java.time.LocalDateTime;

public interface BattlePet {
    Long getId();
    Long getOwnerId();
    long getAttackPower();
    long getLevel();
    
    long getMaxEnergy();
    long getCurrentEnergy();
    void setCurrentEnergy(long value);

    boolean isActive();

    void setLastAttackAt(LocalDateTime time);
}

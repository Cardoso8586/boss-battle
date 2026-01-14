package com.boss_battle.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.boss_battle.model.BossRewardLock;

import jakarta.persistence.LockModeType;

public interface BossRewardLockRepository
extends JpaRepository<BossRewardLock, String> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT l FROM BossRewardLock l WHERE l.bossName = :bossName")
	BossRewardLock lockByBossName(String bossName);

}

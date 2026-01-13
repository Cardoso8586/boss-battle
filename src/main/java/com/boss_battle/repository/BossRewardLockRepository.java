package com.boss_battle.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.boss_battle.model.BossRewardLock;

import jakarta.persistence.LockModeType;

public interface BossRewardLockRepository
extends JpaRepository<BossRewardLock, String> {

	 @Lock(LockModeType.PESSIMISTIC_WRITE)
	    @Query("SELECT b FROM BossRewardLock b WHERE b.bossName = :bossName")
	    BossRewardLock lockByBossName(@Param("bossName") String bossName);
}

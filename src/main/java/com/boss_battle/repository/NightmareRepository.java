package com.boss_battle.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBoss;
import com.boss_battle.model.GlobalBossNightmare;

public interface NightmareRepository extends JpaRepository<GlobalBossNightmare, Long> {
	 List<GlobalBossNightmare> findByAliveTrue();
	   Optional<GlobalBossNightmare> findFirstByAliveTrue();
	Optional<GlobalBoss> findByName(String string);
}

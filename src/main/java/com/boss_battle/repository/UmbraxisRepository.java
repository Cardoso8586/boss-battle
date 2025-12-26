package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBoss;
import com.boss_battle.model.GlobalBossUmbraxis;

public interface UmbraxisRepository extends JpaRepository<GlobalBossUmbraxis, Long> {
	 List<GlobalBossUmbraxis> findByAliveTrue();
	   Optional<GlobalBossUmbraxis> findFirstByAliveTrue();
	Optional<GlobalBoss> findByName(String string);
}

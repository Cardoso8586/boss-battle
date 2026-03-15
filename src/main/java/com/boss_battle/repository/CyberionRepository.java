package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossCyberion;

public interface CyberionRepository extends JpaRepository<GlobalBossCyberion, Long> {
	

	 List<GlobalBossCyberion> findByAliveTrue();

	    Optional<GlobalBossCyberion> findFirstByAliveTrue();

	    Optional<GlobalBossCyberion> findByName(String name);

}
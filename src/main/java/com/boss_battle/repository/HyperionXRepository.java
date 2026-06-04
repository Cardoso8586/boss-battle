package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossHyperionX;

public interface HyperionXRepository extends JpaRepository<GlobalBossHyperionX, Long> {
	
	
	 List<GlobalBossHyperionX> findByAliveTrue();

	    Optional<GlobalBossHyperionX> findFirstByAliveTrue();

	    Optional<GlobalBossHyperionX> findByName(String name);

}

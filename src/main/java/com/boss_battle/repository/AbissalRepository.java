package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossAbissal;

public interface AbissalRepository extends JpaRepository<GlobalBossAbissal, Long> {
	
	
	 List<GlobalBossAbissal> findByAliveTrue();

	    Optional<GlobalBossAbissal> findFirstByAliveTrue();

	    Optional<GlobalBossAbissal> findByName(String name);

}

package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossPuppetrix;

public interface PuppetrixRepository extends JpaRepository<GlobalBossPuppetrix, Long> {
	
	 List<GlobalBossPuppetrix> findByAliveTrue();

	    Optional<GlobalBossPuppetrix> findFirstByAliveTrue();

	    Optional<GlobalBossPuppetrix> findByName(String name);

}
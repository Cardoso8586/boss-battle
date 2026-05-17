package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossTharzul;

public interface TharzulRepository extends JpaRepository<GlobalBossTharzul, Long> {
	
	
	 List<GlobalBossTharzul> findByAliveTrue();

	    Optional<GlobalBossTharzul> findFirstByAliveTrue();

	    Optional<GlobalBossTharzul> findByName(String name);

}

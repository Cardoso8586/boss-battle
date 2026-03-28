package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossVesta;

public interface VestaRepository extends JpaRepository<GlobalBossVesta, Long> {
	
	
	 List<GlobalBossVesta> findByAliveTrue();

	    Optional<GlobalBossVesta> findFirstByAliveTrue();

	    Optional<GlobalBossVesta> findByName(String name);

}

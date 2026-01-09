package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossDestruidor;

public interface DestruidorRepository extends JpaRepository<GlobalBossDestruidor, Long> {
	
	
	 List<GlobalBossDestruidor> findByAliveTrue();

	    Optional<GlobalBossDestruidor> findFirstByAliveTrue();

	    Optional<GlobalBossDestruidor> findByName(String name);

}

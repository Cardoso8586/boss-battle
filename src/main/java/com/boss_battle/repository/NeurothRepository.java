package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossNeuroth;

public interface NeurothRepository extends JpaRepository<GlobalBossNeuroth, Long> {
	
	
	 List<GlobalBossNeuroth> findByAliveTrue();

	    Optional<GlobalBossNeuroth> findFirstByAliveTrue();

	    Optional<GlobalBossNeuroth> findByName(String name);

}

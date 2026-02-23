package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossNecrothar;

public interface NecrotharRepository extends JpaRepository<GlobalBossNecrothar, Long> {
	
	
	 List<GlobalBossNecrothar> findByAliveTrue();

	    Optional<GlobalBossNecrothar> findFirstByAliveTrue();

	    Optional<GlobalBossNecrothar> findByName(String name);

}

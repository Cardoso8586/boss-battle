package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossAbyssar;

public interface AbyssarRepository extends JpaRepository<GlobalBossAbyssar, Long> {
	
	
	 List<GlobalBossAbyssar> findByAliveTrue();

	    Optional<GlobalBossAbyssar> findFirstByAliveTrue();

	    Optional<GlobalBossAbyssar> findByName(String name);

}

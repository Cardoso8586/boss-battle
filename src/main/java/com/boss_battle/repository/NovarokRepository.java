package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossNovarok;

public interface NovarokRepository extends JpaRepository<GlobalBossNovarok, Long> {
	
	
	 List<GlobalBossNovarok> findByAliveTrue();

	    Optional<GlobalBossNovarok> findFirstByAliveTrue();

	    Optional<GlobalBossNovarok> findByName(String name);

}

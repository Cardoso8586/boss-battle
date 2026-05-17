package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossVorgrim;

public interface VorgrimRepository extends JpaRepository<GlobalBossVorgrim, Long> {
	
	   List<GlobalBossVorgrim> findByAliveTrue();

	    Optional<GlobalBossVorgrim> findFirstByAliveTrue();

	    Optional<GlobalBossVorgrim> findByName(String name);

}
package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossMalphion;

public interface MalphionRepository extends JpaRepository<GlobalBossMalphion, Long> {
	
	
	 List<GlobalBossMalphion> findByAliveTrue();

	    Optional<GlobalBossMalphion> findFirstByAliveTrue();

	    Optional<GlobalBossMalphion> findByName(String name);

}

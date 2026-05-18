package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossMorvyra;

public interface MorvyraRepository extends JpaRepository<GlobalBossMorvyra, Long> {
	
	
	 List<GlobalBossMorvyra> findByAliveTrue();

	    Optional<GlobalBossMorvyra> findFirstByAliveTrue();

	    Optional<GlobalBossMorvyra> findByName(String name);

}

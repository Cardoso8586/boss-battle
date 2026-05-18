package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossMalghor;

public interface MalghorRepository extends JpaRepository<GlobalBossMalghor, Long> {
	

	    List<GlobalBossMalghor> findByAliveTrue();

	    Optional<GlobalBossMalghor> findFirstByAliveTrue();

	    Optional<GlobalBossMalghor> findByName(String name);

}
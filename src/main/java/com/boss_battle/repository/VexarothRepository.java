package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossVexaroth;

public interface VexarothRepository extends JpaRepository<GlobalBossVexaroth, Long> {
	

	 List<GlobalBossVexaroth> findByAliveTrue();

	    Optional<GlobalBossVexaroth> findFirstByAliveTrue();

	    Optional<GlobalBossVexaroth> findByName(String name);

}
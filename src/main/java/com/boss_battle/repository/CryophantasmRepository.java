package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossCryophantasm;

public interface CryophantasmRepository extends JpaRepository<GlobalBossCryophantasm, Long> {
	
	
	 List<GlobalBossCryophantasm> findByAliveTrue();

	    Optional<GlobalBossCryophantasm> findFirstByAliveTrue();

	    Optional<GlobalBossCryophantasm> findByName(String name);

}

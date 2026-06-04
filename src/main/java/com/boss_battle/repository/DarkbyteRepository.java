package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossDarkbyte;

public interface DarkbyteRepository extends JpaRepository<GlobalBossDarkbyte, Long> {
	
	
	 List<GlobalBossDarkbyte> findByAliveTrue();

	    Optional<GlobalBossDarkbyte> findFirstByAliveTrue();

	    Optional<GlobalBossDarkbyte> findByName(String name);

}

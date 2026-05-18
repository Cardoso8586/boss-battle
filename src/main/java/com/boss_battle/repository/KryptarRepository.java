package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossKryptar;

public interface KryptarRepository extends JpaRepository<GlobalBossKryptar, Long> {
	

	    List<GlobalBossKryptar> findByAliveTrue();

	    Optional<GlobalBossKryptar> findFirstByAliveTrue();

	    Optional<GlobalBossKryptar> findByName(String name);

}
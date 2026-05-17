package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossEclypzor;

public interface EclypzorRepository extends JpaRepository<GlobalBossEclypzor, Long> {
	

	    List<GlobalBossEclypzor> findByAliveTrue();

	    Optional<GlobalBossEclypzor> findFirstByAliveTrue();

	    Optional<GlobalBossEclypzor> findByName(String name);

}
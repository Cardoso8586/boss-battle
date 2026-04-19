package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossGatalicos;

public interface GatalicosRepository extends JpaRepository<GlobalBossGatalicos, Long> {
	
	
	 List<GlobalBossGatalicos> findByAliveTrue();

	    Optional<GlobalBossGatalicos> findFirstByAliveTrue();

	    Optional<GlobalBossGatalicos> findByName(String name);

}

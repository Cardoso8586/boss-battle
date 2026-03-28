package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossObraserker;

public interface ObraserkerRepository extends JpaRepository<GlobalBossObraserker, Long> {
	

	 List<GlobalBossObraserker> findByAliveTrue();

	    Optional<GlobalBossObraserker> findFirstByAliveTrue();

	    Optional<GlobalBossObraserker> findByName(String name);

}
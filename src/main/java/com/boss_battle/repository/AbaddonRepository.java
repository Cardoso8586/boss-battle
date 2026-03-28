package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossAbaddon;

public interface AbaddonRepository extends JpaRepository<GlobalBossAbaddon, Long> {
	

	    List<GlobalBossAbaddon> findByAliveTrue();

	    Optional<GlobalBossAbaddon> findFirstByAliveTrue();

	    Optional<GlobalBossAbaddon> findByName(String name);

}
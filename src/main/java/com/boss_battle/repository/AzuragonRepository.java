package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossAzuragon;

public interface AzuragonRepository extends JpaRepository<GlobalBossAzuragon, Long> {
	

	    List<GlobalBossAzuragon> findByAliveTrue();

	    Optional<GlobalBossAzuragon> findFirstByAliveTrue();

	    Optional<GlobalBossAzuragon> findByName(String name);

}
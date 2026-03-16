package com.boss_battle.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossNexarach;

public interface NexarachRepository extends JpaRepository<GlobalBossNexarach, Long> {

	 List<GlobalBossNexarach> findByAliveTrue();

	    Optional<GlobalBossNexarach> findFirstByAliveTrue();

	    Optional<GlobalBossNexarach> findByName(String name);
	
	
}
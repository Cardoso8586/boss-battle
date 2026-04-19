package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossVorgathon;

public interface VorgathonRepository extends JpaRepository<GlobalBossVorgathon, Long> {
	

	 List<GlobalBossVorgathon> findByAliveTrue();

	    Optional<GlobalBossVorgathon> findFirstByAliveTrue();

	    Optional<GlobalBossVorgathon> findByName(String name);

}
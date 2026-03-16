package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossOculthar;

public interface OcultharRepository extends JpaRepository<GlobalBossOculthar, Long> {
	
	   List<GlobalBossOculthar> findByAliveTrue();

	    Optional<GlobalBossOculthar> findFirstByAliveTrue();

	    Optional<GlobalBossOculthar> findByName(String name);

}
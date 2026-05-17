package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossApocalyx;

public interface ApocalyxRepository extends JpaRepository<GlobalBossApocalyx, Long> {
	

	    List<GlobalBossApocalyx> findByAliveTrue();

	    Optional<GlobalBossApocalyx> findFirstByAliveTrue();

	    Optional<GlobalBossApocalyx> findByName(String name);

}
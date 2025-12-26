package com.boss_battle.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBoss;
import com.boss_battle.model.GlobalBossNoxar;

public interface NoxarRepository extends JpaRepository<GlobalBossNoxar, Long> {
	 List<GlobalBossNoxar> findByAliveTrue();
	   Optional<GlobalBossNoxar> findFirstByAliveTrue();
	Optional<GlobalBoss> findByName(String string);
}

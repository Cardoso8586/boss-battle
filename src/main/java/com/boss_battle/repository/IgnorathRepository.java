package com.boss_battle.repository;




import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossIgnorath;

public interface IgnorathRepository extends JpaRepository<GlobalBossIgnorath, Long> {
	 List<GlobalBossIgnorath> findByAliveTrue();
	   Optional<GlobalBossIgnorath> findFirstByAliveTrue();
	Optional<GlobalBossIgnorath> findByName(String string);
}

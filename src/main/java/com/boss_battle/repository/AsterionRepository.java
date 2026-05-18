package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossAsterion;

public interface AsterionRepository extends JpaRepository<GlobalBossAsterion, Long> {

    List<GlobalBossAsterion> findByAliveTrue();

    Optional<GlobalBossAsterion> findFirstByAliveTrue();

    Optional<GlobalBossAsterion> findByName(String name);
}

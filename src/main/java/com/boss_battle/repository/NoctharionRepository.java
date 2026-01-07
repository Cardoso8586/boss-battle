package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossNoctharion;

public interface NoctharionRepository extends JpaRepository<GlobalBossNoctharion, Long> {

    List<GlobalBossNoctharion> findByAliveTrue();

    Optional<GlobalBossNoctharion> findFirstByAliveTrue();

    Optional<GlobalBossNoctharion> findByName(String name);
}

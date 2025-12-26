package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossNoctyr;

public interface NoctyrRepository extends JpaRepository<GlobalBossNoctyr, Long> {

    List<GlobalBossNoctyr> findByAliveTrue();

    Optional<GlobalBossNoctyr> findFirstByAliveTrue();

    Optional<GlobalBossNoctyr> findByName(String name);
}

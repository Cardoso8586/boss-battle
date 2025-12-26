package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossAzurion;

public interface AzurionRepository extends JpaRepository<GlobalBossAzurion, Long> {

    List<GlobalBossAzurion> findByAliveTrue();

    Optional<GlobalBossAzurion> findFirstByAliveTrue();

    Optional<GlobalBossAzurion> findByName(String name);
}

package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossOblivar;

public interface OblivarRepository extends JpaRepository<GlobalBossOblivar, Long> {

    List<GlobalBossOblivar> findByAliveTrue();

    Optional<GlobalBossOblivar> findFirstByAliveTrue();

    Optional<GlobalBossOblivar> findByName(String name);
}

package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossInfernax;

public interface InfernaxRepository extends JpaRepository<GlobalBossInfernax, Long> {

    List<GlobalBossInfernax> findByAliveTrue();

    Optional<GlobalBossInfernax> findFirstByAliveTrue();

    Optional<GlobalBossInfernax> findByName(String name);
}

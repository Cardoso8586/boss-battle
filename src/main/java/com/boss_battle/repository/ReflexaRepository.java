package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossReflexa;

public interface ReflexaRepository extends JpaRepository<GlobalBossReflexa, Long> {

    List<GlobalBossReflexa> findByAliveTrue();

    Optional<GlobalBossReflexa> findFirstByAliveTrue();

    Optional<GlobalBossReflexa> findByName(String name);
}

package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossXaphiron;

public interface XaphironRepository extends JpaRepository<GlobalBossXaphiron, Long> {

    List<GlobalBossXaphiron> findByAliveTrue();

    Optional<GlobalBossXaphiron> findFirstByAliveTrue();

    Optional<GlobalBossXaphiron> findByName(String name);
}

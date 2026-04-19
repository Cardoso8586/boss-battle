package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossXarvokth;

public interface XarvokthRepository extends JpaRepository<GlobalBossXarvokth, Long> {

    List<GlobalBossXarvokth> findByAliveTrue();

    Optional<GlobalBossXarvokth> findFirstByAliveTrue();

    Optional<GlobalBossXarvokth> findByName(String name);
}

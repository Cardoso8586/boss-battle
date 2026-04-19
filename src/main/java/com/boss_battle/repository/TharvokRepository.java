package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossTharvok;

public interface TharvokRepository extends JpaRepository<GlobalBossTharvok, Long> {

    List<GlobalBossTharvok> findByAliveTrue();

    Optional<GlobalBossTharvok> findFirstByAliveTrue();

    Optional<GlobalBossTharvok> findByName(String name);
}

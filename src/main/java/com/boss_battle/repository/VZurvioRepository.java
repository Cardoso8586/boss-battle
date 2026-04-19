package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossVZurvio;

public interface VZurvioRepository extends JpaRepository<GlobalBossVZurvio, Long> {

    List<GlobalBossVZurvio> findByAliveTrue();

    Optional<GlobalBossVZurvio> findFirstByAliveTrue();

    Optional<GlobalBossVZurvio> findByName(String name);
}

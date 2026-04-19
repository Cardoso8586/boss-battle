package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossVorlath;

public interface VorlathRepository extends JpaRepository<GlobalBossVorlath, Long> {

    List<GlobalBossVorlath> findByAliveTrue();

    Optional<GlobalBossVorlath> findFirstByAliveTrue();

    Optional<GlobalBossVorlath> findByName(String name);
}

package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossExodrax;

public interface ExodraxRepository extends JpaRepository<GlobalBossExodrax, Long> {

    List<GlobalBossExodrax> findByAliveTrue();

    Optional<GlobalBossExodrax> findFirstByAliveTrue();

    Optional<GlobalBossExodrax> findByName(String name);
}

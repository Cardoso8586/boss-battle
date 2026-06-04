package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossNexaron;

public interface NexaronRepository extends JpaRepository<GlobalBossNexaron, Long> {

    List<GlobalBossNexaron> findByAliveTrue();

    Optional<GlobalBossNexaron> findFirstByAliveTrue();

    Optional<GlobalBossNexaron> findByName(String name);
}

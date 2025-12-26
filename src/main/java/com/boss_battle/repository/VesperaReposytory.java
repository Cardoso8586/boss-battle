package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossVespera;

public interface VesperaReposytory extends JpaRepository<GlobalBossVespera, Long> {

    List<GlobalBossVespera> findByAliveTrue();

    Optional<GlobalBossVespera> findFirstByAliveTrue();

    Optional<GlobalBossVespera> findByName(String name);
}

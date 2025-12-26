package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossOblivion;

public interface OblivionRepository extends JpaRepository<GlobalBossOblivion, Long> {

    List<GlobalBossOblivion> findByAliveTrue();

    Optional<GlobalBossOblivion> findFirstByAliveTrue();

    Optional<GlobalBossOblivion> findByName(String name);
}

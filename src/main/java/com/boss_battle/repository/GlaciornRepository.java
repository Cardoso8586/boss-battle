package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossGlaciorn;

public interface GlaciornRepository extends JpaRepository<GlobalBossGlaciorn, Long> {

    List<GlobalBossGlaciorn> findByAliveTrue();

    Optional<GlobalBossGlaciorn> findFirstByAliveTrue();

    Optional<GlobalBossGlaciorn> findByName(String name);
}

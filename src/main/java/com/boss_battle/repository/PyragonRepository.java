package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossPyragon;

public interface PyragonRepository extends JpaRepository<GlobalBossPyragon, Long> {

    List<GlobalBossPyragon> findByAliveTrue();

    Optional<GlobalBossPyragon> findFirstByAliveTrue();

    Optional<GlobalBossPyragon> findByName(String name);
}

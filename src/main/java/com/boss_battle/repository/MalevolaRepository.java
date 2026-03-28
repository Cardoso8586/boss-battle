package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossMalevola;

public interface MalevolaRepository extends JpaRepository<GlobalBossMalevola, Long> {

    List<GlobalBossMalevola> findByAliveTrue();

    Optional<GlobalBossMalevola> findFirstByAliveTrue();

    Optional<GlobalBossMalevola> findByName(String name);
}

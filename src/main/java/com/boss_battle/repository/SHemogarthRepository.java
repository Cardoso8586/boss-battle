package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossSHemogarth;

public interface SHemogarthRepository extends JpaRepository<GlobalBossSHemogarth, Long> {

    List<GlobalBossSHemogarth> findByAliveTrue();

    Optional<GlobalBossSHemogarth> findFirstByAliveTrue();

    Optional<GlobalBossSHemogarth> findByName(String name);
}

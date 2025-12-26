package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossMorvath;

public interface MorvathRepository extends JpaRepository<GlobalBossMorvath, Long> {

    List<GlobalBossMorvath> findByAliveTrue();

    Optional<GlobalBossMorvath> findFirstByAliveTrue();

    Optional<GlobalBossMorvath> findByName(String name);
}

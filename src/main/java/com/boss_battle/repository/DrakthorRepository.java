package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossDrakthor;

public interface DrakthorRepository extends JpaRepository<GlobalBossDrakthor, Long> {

    List<GlobalBossDrakthor> findByAliveTrue();

    Optional<GlobalBossDrakthor> findFirstByAliveTrue();

    Optional<GlobalBossDrakthor> findByName(String name);
}

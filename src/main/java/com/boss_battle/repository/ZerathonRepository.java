package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossZerathon;

public interface ZerathonRepository extends JpaRepository<GlobalBossZerathon, Long> {

    List<GlobalBossZerathon> findByAliveTrue();

    Optional<GlobalBossZerathon> findFirstByAliveTrue();

    Optional<GlobalBossZerathon> findByName(String name);
}

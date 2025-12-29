package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossThunderon;

public interface ThunderonRepository extends JpaRepository<GlobalBossThunderon, Long> {

    List<GlobalBossThunderon> findByAliveTrue();

    Optional<GlobalBossThunderon> findFirstByAliveTrue();

    Optional<GlobalBossThunderon> findByName(String name);
}

package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossArenascor;

public interface ArenascorRepository extends JpaRepository<GlobalBossArenascor, Long> {

    List<GlobalBossArenascor> findByAliveTrue();

    Optional<GlobalBossArenascor> findFirstByAliveTrue();

    Optional<GlobalBossArenascor> findByName(String name);
}

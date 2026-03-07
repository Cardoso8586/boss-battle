package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossLeviatanAbismo;


public interface LeviatanAbismoRepository extends JpaRepository<GlobalBossLeviatanAbismo, Long> {

    List<GlobalBossLeviatanAbismo> findByAliveTrue();

    Optional<GlobalBossLeviatanAbismo> findFirstByAliveTrue();

    Optional<GlobalBossLeviatanAbismo> findByName(String name);
}

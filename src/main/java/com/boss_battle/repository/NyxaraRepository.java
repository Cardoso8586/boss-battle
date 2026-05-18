package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossNyxara;

public interface NyxaraRepository extends JpaRepository<GlobalBossNyxara, Long> {

    List<GlobalBossNyxara> findByAliveTrue();

    Optional<GlobalBossNyxara> findFirstByAliveTrue();

    Optional<GlobalBossNyxara> findByName(String name);
}

package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossLyxara;

public interface LyxaraRepository extends JpaRepository<GlobalBossLyxara, Long> {

    List<GlobalBossLyxara> findByAliveTrue();

    Optional<GlobalBossLyxara> findFirstByAliveTrue();

    Optional<GlobalBossLyxara> findByName(String name);
}

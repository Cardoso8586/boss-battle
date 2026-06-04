package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossVoidreaper;

public interface VoidreaperRepository extends JpaRepository<GlobalBossVoidreaper, Long> {

    List<GlobalBossVoidreaper> findByAliveTrue();

    Optional<GlobalBossVoidreaper> findFirstByAliveTrue();

    Optional<GlobalBossVoidreaper> findByName(String name);
}

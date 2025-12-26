package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossGlaciara;

public interface GlaciaraRepository extends JpaRepository<GlobalBossGlaciara, Long> {

    List<GlobalBossGlaciara> findByAliveTrue();

    Optional<GlobalBossGlaciara> findFirstByAliveTrue();

    Optional<GlobalBossGlaciara> findByName(String name);
}
